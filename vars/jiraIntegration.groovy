def commentInitBuild() {
  if(validJiraConfig()) {
    withEnv(['JIRA_SITE=JIRA-CTTI']) {
      jiraAddComment(
        idOrKey: params.JIRA_ISSUE_KEY,
        input: "EXECUCIÓ INICIADA (#${env.BUILD_NUMBER})"
      )
    }
  }
}

def uploadResults(String resultFile) {
  step([$class: 'XrayImportBuilder',
    serverInstance: '226faf03-9189-4ee8-964e-1a691d60f62d',
    projectKey: params.JIRA_PROJECT_KEY,
    testPlanKey: params.JIRA_ISSUE_KEY,
    importFilePath: resultFile
  ])
}

private Boolean validJiraConfig() {
  return params.JIRA_PROJECT_KEY?.trim() && params.JIRA_ISSUE_KEY?.trim()
}
