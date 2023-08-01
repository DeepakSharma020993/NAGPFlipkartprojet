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
                withSonarQubeEnv('test Sonar') {
                    bat 'mvn sonar:sonar -Dsonar.login=sqa_5a2d7aaa043f45a5525ac7a3152da861dfa6f5be'
                }
            }
        }
        stage('Run Tests') {
            steps {
                echo 'Starting Tests'
                bat 'mvn test'
            }
        }

        stage('Quality Gate') {
            steps {
                echo 'Checking Quality Gate'
                withSonarQubeEnv('test Sonar') {
                    bat 'mvn verify -DskipTests'
                }
            }
        }
        
        stage('Artifactory Upload') {
            steps {
                echo 'Starting artifact upload to Artifactory'
                
                bat 'mvn deploy -Dmaven.deploy.skip=true -Dmaven.repo.url=http://localhost:8082/artifactory -Dmaven.repo.username=admin -Dmaven.repo.password=Deep@1234'
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
