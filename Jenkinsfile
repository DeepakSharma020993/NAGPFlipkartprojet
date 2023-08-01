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
                echo 'Starting the build using Maven'
                bat 'mvn clean package -DskipTests'
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



        
    }

    post {
        success {
            echo 'Pipeline execution succeeded!'
            // Add any post-success actions or notifications here
        }

        failure {
            echo 'Pipeline execution failed!'
            // Add any post-failure actions or notifications here
        }
    }
}
