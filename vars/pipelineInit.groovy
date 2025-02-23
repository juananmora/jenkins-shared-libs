def call(Map config) {
  properties([
    parameters([
      string(name: 'REPO_URL', defaultValue: config.repoUrl),
      string(name: 'BRANCH', defaultValue: 'master'),
      choice(name: 'ENV_TO_TEST', choices: ['Desenvolupament','Integracio','Preproduccio','Produccio']),
      booleanParam(name: 'QUALITY_GATE', defaultValue: true),
      string(name: 'FAILURE_THRESHOLD', defaultValue: '20')
    ] + config.extraParams)
  ])
  
  timestamps {
    try {
      podTemplate(yaml: libraryResource("templates/${config.podTemplate}")) {
        node(config.nodeLabel) {
          stage('Configuración Inicial') {
            validationUtils.validateRequiredParams(config.requiredParams)
            jiraIntegration.commentInitBuild()
          }
          // Resto del pipeline...
        }
      }
    } catch(Exception e) {
      jiraIntegration.reportError(e)
      throw e
    }
  }

  stage('Checkout') {
    script {
      def credId = CredentialManager.getRepoCredential(params.REPO_URL)
      checkout scm: [
        $class: 'GitSCM',
        branches: [[name: params.BRANCH]],
        extensions: [],
        userRemoteConfigs: [[
          url: params.REPO_URL,
          credentialsId: credId
        ]]
      ]
    }
  }
}
