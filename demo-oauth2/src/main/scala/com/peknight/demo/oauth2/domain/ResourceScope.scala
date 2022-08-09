package com.peknight.demo.oauth2.domain

case class ResourceScope(resource: Resource, scope: Option[Set[String]])
