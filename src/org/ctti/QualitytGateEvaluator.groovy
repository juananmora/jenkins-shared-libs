package org.ctti

class QualityGateEvaluator {
  static void evaluateFunctionalTests(String xmlReport) {
    def results = new XmlSlurper().parse(new File(xmlReport))
    def failureRate = (results.@failures.toInteger() / results.@tests.toInteger()) * 100
    
    if(failureRate > PipelineConstants.FUNCTIONAL_FAILURE_THRESHOLD) {
      error("Quality gate failed: ${failureRate}% test failures")
    }
  }
}
