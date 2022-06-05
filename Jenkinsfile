properties([pipelineTriggers([githubPush()])])
pipeline {
    agent any
    environment {
        registryUrl = "hidpdeveastusbotacr.azurecr.io"
        
        }
    
        stages {
          stage( 'Gitcheckout') {
                steps {
                 //   checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'test-tken-v', url: '']]])
                    echo "test"
                }
            }  
        stage( 'Build') {
                steps {
                   sh "mvn clean install -DskipTests=True"
                }
            }
              stage('SonarQube analysis') {
                steps {
                    withSonarQubeEnv('sonarqube-9.0.1') {
                    sh "mvn sonar:sonar -Dsonar.projectKey=maven-demo -Dsonar.host.url=http://20.62.94.77:9000 -Dsonar.login=158efdc21d2439bc98382b933222fcc94655ddea"
    }
        }
        }
            stage( 'Build docker image') {
                steps {
                    sh "docker build -t $registryUrl/hello:${BUILD_NUMBER} ."
                    
                }
                
            }
            stage('Upload Image to ACR') {
                steps{
                    withCredentials([usernamePassword(credentialsId: 'docker-hub', passwordVariable: 'docker_pass', usernameVariable: 'docker_user')]) {
                        sh "docker login $registryUrl -u ${docker_user} -p ${docker_pass}"
}
                    
                  //  sh 'docker tag  hello:latest $registryUrl/hello:${BUILD_NUMBER}'
                    sh 'docker push $registryUrl/hello:${BUILD_NUMBER}'
                    
                }
            }
            stage( 'Login to AKS repo') {
                steps {
                        sh 'rm -rf *'
                     withCredentials([usernamePassword(credentialsId: 'test-tken-v', passwordVariable: 'password', usernameVariable: 'username')]) {
                      // git remote set-url origin https://venkateshmuddusetty:${password}@github.com/venkateshmuddusetty/test.git
                         sh '''  
                         git config --global user.name "${username}"
                         git config --global user.email "venkat149dev@gmail.com"
                         git clone https://${password}@github.com/venkateshmuddusetty/test.git
                          '''
                     } 
                }
            }
            
            stage( 'Update to AKS repo') {
                steps {
                    sh '''
                        cd test/
                         git branch
                        
                         sed -i "s+hidpdeveastusbotacr.azurecr.io/hello.*+$registryUrl/hello:${BUILD_NUMBER}+g" ${WORKSPACE}/test/deployment.yml
                         cat deployment.yml
                         git add deployment.yml
                         git commit -m "Build_number"
                         git push -u origin '''
                    
                }
            } 
        }
}
