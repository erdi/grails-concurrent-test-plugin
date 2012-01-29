loadConcurrentTestTypeClass = {->
    def doLoad = {-> classLoader.loadClass('grails.plugin.concurrenttest.ConcurrentJUnitTestType') }
    try {
        doLoad()
    } catch (ClassNotFoundException e) {
        includeTargets << grailsScript("_GrailsCompile")
        compile()
        doLoad()
    }
}

loadConcurrentTestType = {
    if (!binding.variables.containsKey("unitTests")) return
    def testTypeClass = loadConcurrentTestTypeClass()
    if (!unitTests.any { it.class == testTypeClass }) {
        if (!config.size()) {
            includeTargets << grailsScript('_GrailsPackage')
            compile()
            createConfig()
        }
        unitTests << testTypeClass.newInstance('concurrent', 'unit', config, true)
        unitTests << testTypeClass.newInstance('concurrent-explicit', 'unit', config, false)
    }
}

eventAllTestsStart = {
    loadConcurrentTestType()
}