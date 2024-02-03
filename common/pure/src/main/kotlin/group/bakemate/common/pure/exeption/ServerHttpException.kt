package group.bakemate.common.pure.exeption

import java.lang.Exception

class ServerHttpException(val status: HttpStatus) : Exception()