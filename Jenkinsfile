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
                sh 'mvn clean package'
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
