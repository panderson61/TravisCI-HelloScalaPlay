
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/jreyes/Desktop/TravisCI-HelloScalaPlay/conf/routes
// @DATE:Thu Jun 09 11:14:42 PDT 2016


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
