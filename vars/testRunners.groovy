def runJMeter(Map args) {
  withCredentials([string(credentialsId: 'influxdb-token', variable: 'INFLUXDB_TOKEN')]) {
    sh """
      jmeter -n -t ${args.testPlan} \\
        -JthreadCount=${args.threads} \\
        -JinfluxdbToken=${INFLUXDB_TOKEN} \\
        -l log.jtl -o html.d
    """
  }
}

def runNewman(Map args) {
  withCredentials([usernamePassword(credentialsId: 'influxdb-creds', usernameVariable: 'INFLUXDB_USER', passwordVariable: 'INFLUXDB_PASS')]) {
    sh """
      newman run ${args.collection} -e ${args.environment} \\
        --reporters cli,junitxray \\
        --reporter-influxdb-token ${INFLUXDB_PASS}
    """
  }
}

def runSeleniumTests(Map args) {
  CredentialManager.withInfluxCredentials { token ->
    sh """
      mvn clean test \\
        -Denvironment=${args.env} \\
        -Dapp_url=${args.appUrl} \\
        -DinfluxdbToken=${token} \\
        -Dsurefire.reportFormat=xml
    """
  }
}

