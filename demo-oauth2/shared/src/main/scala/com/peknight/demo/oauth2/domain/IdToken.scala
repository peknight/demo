package com.peknight.demo.oauth2.domain

case class IdToken(
                    content: String = "{}",
                    // iss: 令牌颁发者。它表示该令牌是由谁创建的，在很多OAuth部署中会将它设为授权服务器的URL。该声明是一个字符串
                    issuer: Option[String] = None,
                    // sub: 令牌的主体。它表示该令牌是关于谁的，在很多OAuth部署中会将它设为资源拥有者的唯一标识。
                    // 在大多数情况下，主体在同一个颁发者的范围内必须是唯一的。该声明是一个字符串
                    subject: Option[String] = None,
                    // aud: 令牌的受众。它表示令牌的接收者，在很多OAuth部署中，它包含受保护资源的URI或者能够接收该令牌的受保护资源。
                    // 该声明可以是一个字符串数组，如果只有一个值，也可以是一个不用数组包装的单个字符串
                    audience: Option[Set[String]] = None,
                    // exp: 令牌的过期时间戳。它表示令牌将在何时过期，以便部署应用让令牌自行失效。
                    // 该声明是一个整数，表示自UNIX新纪元（即格林威治标准时间GMT，1970年1月1日零点）以来的秒数
                    expiration: Option[Long] = None,
                    // nbf: 令牌生效时的时间戳。它表示令牌从什么时候开始生效，以便部署应用可以在令牌生效之前颁发令牌。
                    // 该声明是一个整数，表示自UNIX新纪元（即格林威治标准时间GMT，1970年1月1日零点）以来的秒数
                    notBefore: Option[Long] = None,
                    // iat: 令牌颁发的时间戳。它表示令牌是何时被创建的，它通常是颁发者在生成令牌时的系统时间戳。
                    // 该声明是一个整数，表示自UNIX新纪元（即格林威治标准时间GMT，1970年1月1日零点）以来的秒数
                    issuedAt: Option[Long] = None,
                    // jti: 令牌的唯一标识符。该声明的值在令牌颁发者创建的每一个令牌中都是唯一的，为了防止冲突，它通常是一个密码学的随机值。
                    // 这个值相当于向结构化的令牌中加入了一个攻击者无法获得的随机熵组件，有利于防止令牌猜测攻击和重放攻击
                    jwtId: Option[String] = None
                  )
