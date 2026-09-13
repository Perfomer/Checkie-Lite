#!/usr/bin/env sh

set -u

TARGET_SERIAL="${PIXEL_ADB_SERIAL:-3B051FDJH000UW}"
PAIR_CODE="${PIXEL_ADB_PAIR_CODE:-}"
CONNECT_ENDPOINT="${PIXEL_ADB_ENDPOINT:-}"
PAIR_ENDPOINT="${PIXEL_ADB_PAIR_ENDPOINT:-}"
TIMEOUT_SECONDS="${PIXEL_ADB_TIMEOUT:-20}"
ADB_CALL_TIMEOUT="${PIXEL_ADB_CALL_TIMEOUT:-10}"

usage() {
    cat <<'EOF'
Connect the configured Pixel 8 using Android wireless debugging.

Usage:
  sh scripts/connect-pixel-wifi.sh [options]

Options:
  --code CODE          Pairing code shown by Android
  --connect HOST:PORT  Connect endpoint when mDNS is unavailable
  --pair HOST:PORT     Pairing endpoint when mDNS is unavailable
  --timeout SECONDS    Total discovery timeout (default: 20)
  -h, --help           Show this help

Environment variables:
  PIXEL_ADB_SERIAL, PIXEL_ADB_PAIR_CODE, PIXEL_ADB_ENDPOINT,
  PIXEL_ADB_PAIR_ENDPOINT, PIXEL_ADB_TIMEOUT, PIXEL_ADB_CALL_TIMEOUT, ADB
EOF
}

fail() {
    printf 'Error: %s\n' "$*" >&2
    exit 1
}

while [ "$#" -gt 0 ]; do
    case "$1" in
        --code)
            [ "$#" -ge 2 ] || fail "--code requires a value"
            PAIR_CODE="$2"
            shift 2
            ;;
        --connect)
            [ "$#" -ge 2 ] || fail "--connect requires HOST:PORT"
            CONNECT_ENDPOINT="$2"
            shift 2
            ;;
        --pair)
            [ "$#" -ge 2 ] || fail "--pair requires HOST:PORT"
            PAIR_ENDPOINT="$2"
            shift 2
            ;;
        --timeout)
            [ "$#" -ge 2 ] || fail "--timeout requires a number"
            TIMEOUT_SECONDS="$2"
            shift 2
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        *)
            fail "unknown option: $1"
            ;;
    esac
done

case "$TIMEOUT_SECONDS" in
    ''|*[!0-9]*) fail "timeout must be a non-negative integer" ;;
esac

case "$ADB_CALL_TIMEOUT" in
    ''|*[!0-9]*|0) fail "PIXEL_ADB_CALL_TIMEOUT must be a positive integer" ;;
esac

find_adb() {
    if [ -n "${ADB:-}" ]; then
        [ -x "$ADB" ] || fail "ADB points to a non-executable file: $ADB"
        printf '%s\n' "$ADB"
        return
    fi

    if command -v adb >/dev/null 2>&1; then
        command -v adb
        return
    fi

    for candidate in \
        "${ANDROID_SDK_ROOT:-}/platform-tools/adb" \
        "${ANDROID_HOME:-}/platform-tools/adb" \
        "${LOCALAPPDATA:-}/Android/Sdk/platform-tools/adb.exe" \
        "$HOME/Android/Sdk/platform-tools/adb" \
        "$HOME/Library/Android/sdk/platform-tools/adb"
    do
        if [ -n "$candidate" ] && [ -x "$candidate" ]; then
            printf '%s\n' "$candidate"
            return
        fi
    done

    fail "adb was not found; install Android SDK Platform-Tools or set ADB"
}

ADB_BIN="$(find_adb)"

adb_call() {
    if command -v timeout >/dev/null 2>&1; then
        timeout "$ADB_CALL_TIMEOUT" "$ADB_BIN" "$@"
    else
        "$ADB_BIN" "$@"
    fi
}

device_is_connected() {
    for device in $(adb_call devices 2>/dev/null | awk '$2 == "device" { print $1 }'); do
        case "$device" in
            *"$TARGET_SERIAL"*) return 0 ;;
        esac

        actual_serial="$(adb_call -s "$device" shell getprop ro.serialno 2>/dev/null | tr -d '\r')"
        if [ "$actual_serial" = "$TARGET_SERIAL" ]; then
            return 0
        fi
    done
    return 1
}

print_target_device() {
    for device in $(adb_call devices 2>/dev/null | awk '$2 == "device" { print $1 }'); do
        case "$device" in
            *"$TARGET_SERIAL"*)
                adb_call devices -l | awk -v target="$device" '$1 == target'
                return
                ;;
        esac

        actual_serial="$(adb_call -s "$device" shell getprop ro.serialno 2>/dev/null | tr -d '\r')"
        if [ "$actual_serial" = "$TARGET_SERIAL" ]; then
            adb_call devices -l | awk -v target="$device" '$1 == target'
            return
        fi
    done
}

discover_endpoint() {
    service_type="$1"
    adb_call mdns services 2>/dev/null | awk \
        -v serial="$TARGET_SERIAL" \
        -v service_type="$service_type" '
        index($1, serial) > 0 && $2 == service_type { endpoint = $3 }
        END { if (endpoint != "") print endpoint }
    '
}

wait_for_endpoint() {
    service_type="$1"
    elapsed=0
    printf 'Waiting for Pixel 8 ADB service (up to %s seconds)...\n' "$TIMEOUT_SECONDS" >&2
    while [ "$elapsed" -le "$TIMEOUT_SECONDS" ]; do
        endpoint="$(discover_endpoint "$service_type")"
        if [ -n "$endpoint" ]; then
            printf '%s\n' "$endpoint"
            return 0
        fi
        [ "$elapsed" -eq "$TIMEOUT_SECONDS" ] && break
        if [ "$elapsed" -gt 0 ] && [ $((elapsed % 5)) -eq 0 ]; then
            printf 'Still waiting... %s/%s seconds\n' "$elapsed" "$TIMEOUT_SECONDS" >&2
        fi
        sleep 1
        elapsed=$((elapsed + 1))
    done
    return 1
}

wait_for_pixel_service() {
    elapsed=0
    printf 'Searching for Pixel 8 over Wi-Fi (up to %s seconds)...\n' "$TIMEOUT_SECONDS" >&2
    while [ "$elapsed" -le "$TIMEOUT_SECONDS" ]; do
        endpoint="$(discover_endpoint '_adb-tls-connect._tcp')"
        if [ -n "$endpoint" ]; then
            printf 'connect %s\n' "$endpoint"
            return 0
        fi

        endpoint="$(discover_endpoint '_adb-tls-pairing._tcp')"
        if [ -n "$endpoint" ]; then
            printf 'pair %s\n' "$endpoint"
            return 0
        fi

        [ "$elapsed" -eq "$TIMEOUT_SECONDS" ] && break
        if [ "$elapsed" -gt 0 ] && [ $((elapsed % 5)) -eq 0 ]; then
            printf 'Still searching... %s/%s seconds\n' "$elapsed" "$TIMEOUT_SECONDS" >&2
        fi
        sleep 1
        elapsed=$((elapsed + 1))
    done
    return 1
}

connect_and_verify() {
    endpoint="$1"
    printf 'Connecting to Pixel 8 at %s...\n' "$endpoint"
    adb_call connect "$endpoint" >/dev/null 2>&1 || return 1

    elapsed=0
    while [ "$elapsed" -le 10 ]; do
        if device_is_connected; then
            return 0
        fi
        sleep 1
        elapsed=$((elapsed + 1))
    done
    return 1
}

adb_call start-server >/dev/null 2>&1 || fail "could not start the adb server"

if device_is_connected; then
    printf 'Pixel 8 (%s) is already connected over ADB.\n' "$TARGET_SERIAL"
    print_target_device
    exit 0
fi

if [ -z "$CONNECT_ENDPOINT" ] && [ -z "$PAIR_ENDPOINT" ]; then
    service="$(wait_for_pixel_service || true)"
    service_type="${service%% *}"
    service_endpoint="${service#* }"
    case "$service_type" in
        connect) CONNECT_ENDPOINT="$service_endpoint" ;;
        pair) PAIR_ENDPOINT="$service_endpoint" ;;
    esac
fi

if [ -n "$CONNECT_ENDPOINT" ] && connect_and_verify "$CONNECT_ENDPOINT"; then
    printf 'Pixel 8 connected successfully.\n'
    print_target_device
    exit 0
fi

if [ -z "$PAIR_ENDPOINT" ]; then
    PAIR_ENDPOINT="$(discover_endpoint '_adb-tls-pairing._tcp')"
fi

if [ -z "$PAIR_ENDPOINT" ]; then
    fail "Pixel 8 was not discovered. On the phone open Settings > System > Developer options > Wireless debugging, enable it, and keep that screen open. Ensure both devices use the same Wi-Fi network."
fi

if [ -z "$PAIR_CODE" ]; then
    if [ -t 0 ]; then
        printf 'Enter the six-digit pairing code shown on the Pixel 8: '
        IFS= read -r PAIR_CODE
    else
        fail "pairing is required; rerun interactively or pass --code CODE"
    fi
fi

case "$PAIR_CODE" in
    *[!0-9]*|'') fail "pairing code must contain digits only" ;;
esac

printf 'Pairing with Pixel 8 at %s...\n' "$PAIR_ENDPOINT"
adb_call pair "$PAIR_ENDPOINT" "$PAIR_CODE" || fail "pairing failed; refresh the pairing code on the phone and try again"

CONNECT_ENDPOINT="$(wait_for_endpoint '_adb-tls-connect._tcp' || true)"
[ -n "$CONNECT_ENDPOINT" ] || fail "pairing succeeded, but the connect service was not discovered; keep Wireless debugging enabled and rerun the script"

if connect_and_verify "$CONNECT_ENDPOINT"; then
    printf 'Pixel 8 paired and connected successfully.\n'
    print_target_device
    exit 0
fi

fail "Pixel 8 was paired but could not be connected at $CONNECT_ENDPOINT"
