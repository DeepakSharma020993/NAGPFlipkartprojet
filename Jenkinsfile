pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
               echo 'Checking out the code from the public GitHub repository'
               git url: 'https://github.com/DeepakSharma020993/NAGPFlipkartprojet.git'
            }
        }

        stage('Build') {
            steps {
                // Display a message to indicate the build is starting
        		echo 'Starting the build using Maven'

       		    // Build your Mavenized code using Maven
       		    sh 'mvn clean package'
            }
        }

        stage('SonarQube Analysis') {
             steps {
            
             echo 'Starting SonarQube code analysis'

     
              withSonarQubeEnv('http://localhost:9000') {
            sh 'mvn sonar:sonar -Dsonar.login=sqa_5a2d7aaa043f45a5525ac7a3152da861dfa6f5be'
        }
    }
        }

        stage('Run Tests') {
            steps {
                echo 'Starting Test'
                sh 'mvn test'
            }
        }

        stage('Quality Gate') {
            steps {
               withSonarQubeEnv('http://localhost:9000') {
                             sh 'mvn sonar:quality-gate'
                         }
            }
        }

        stage('Artifactory Upload') {
    steps {
        // Display a message to indicate the artifact upload is starting
        echo 'Starting artifact upload to Artifactory'

        // Upload the artifacts to Artifactory if the build is successful and SonarQube quality gate passes
        // Replace 'your_artifactory_url' with the URL of your Artifactory repository
        // Replace 'your_artifactory_username' and 'your_artifactory_password' with the appropriate Artifactory credentials
        // Example: sh 'mvn deploy -Dmaven.deploy.skip=true -Dmaven.repo.url=http://localhost:8082/artifactory -Dmaven.repo.username=admin -Dmaven.repo.password=Deep@1234'
    }
}
        
    }

    post {
        always {
            // Ensure the test results are visible on the SonarQube dashboard
            // Example: junit '**/target/surefire-reports/*.xml'
        }

        success {
            // If build is successful, add post-build actions here (optional)
            // Example: emailext body: 'The Jenkins build is successful!', subject: 'Build Status', to: 'user@example.com'
        }

        failure {
            // If build fails, fail the pipeline explicitly
            // Example: error('Build failed due to Sonar analysis or test failures.')
        }
    }
}
