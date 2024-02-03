package group.bakemate.feature.registration.presentation.entity

internal sealed interface Condition {

    object Empty : Condition

    object Correct : Condition
}

internal sealed interface EmailCondition : Condition {

    object NotEmail : EmailCondition
}

internal sealed interface NicknameCondition : Condition {

    object Short : NicknameCondition
}

internal sealed interface PasswordCondition : Condition {

    object Short : PasswordCondition
}

internal sealed interface ConfirmPasswordCondition : Condition {

    object NotMatch : ConfirmPasswordCondition
}
