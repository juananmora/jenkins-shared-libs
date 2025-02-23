package org.ctti

class CredentialManager {
  static String getRepoCredential(String repoUrl) {
    switch(repoUrl) {
      case ~/.*github\.com.*/: return 'githubMAT'
      case ~/.*gitlab\.com.*/: return 'gitlabMAT'
      default: throw new Exception("No credentials configured for repository host")
    }
  }

  static withInfluxCredentials(Closure action) {
    withCredentials([
      string(credentialsId: 'influxdb-token', variable: 'INFLUXDB_TOKEN')
    ]) {
      action.call(env.INFLUXDB_TOKEN)
    }
  }

  static withJiraCredentials(Closure action) {
    withCredentials([
      usernamePassword(
        credentialsId: 'jira-step',
        usernameVariable: 'JIRA_USER',
        passwordVariable: 'JIRA_API_TOKEN'
      )
    ]) {
      action.call(env.JIRA_USER, env.JIRA_API_TOKEN)
    }
  }
}
