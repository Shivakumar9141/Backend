properties([pipelineTriggers([githubPush()])])
pipeline {
    agent any
    environment {
        registryUrl = "hidpdeveastusazcrvvd.azurecr.io"
        
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
                    script {
                        datas = readYaml (file : "$WORKSPACE/config.yml")
                        echo "build type is: ${datas.Build_type}"
                        
                        
                        if( "${datas.Build_type}" == "maven" )
                        {
                        sh 'mvn clean install -DskipTests=True'
                        }
                        else( "${datas.Build_type}" == "gradle" ) 
                        {    
                        sh 'gradle build'
                        }
                        
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
                     withCredentials([usernamePassword(credentialsId: 'test', passwordVariable: 'password', usernameVariable: 'username')]) {
                      // git remote set-url origin https://Shivakumar9141:${password}@github.com/Shivakumar9141/test.git
                         sh '''  
                         git config --global user.name "${username}"
                         git config --global user.email "shivakumaras444@gmail.com"
                         git clone https://${password}@github.com/Shivakumar9141/test.git
                          '''
                     } 
                }
            }
            
            stage( 'Update with version to AKS repo') {
                steps {
                    sh '''
                        cd test/
                         git branch
                        
                         sed -i "s+hidpdeveastusazcrvvd.azurecr.io/hello.*+$registryUrl/hello:${BUILD_NUMBER}+g" ${WORKSPACE}/test/deployment.yml
                         cat deployment.yml
                         git add deployment.yml
                         git commit -m "Build_number"
                         git push -u origin '''
                    
                }
            } 
        }
}
