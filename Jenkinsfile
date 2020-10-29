pipeline {
    agent {
        docker {
            image 'base/builder:1.0.0'
            args '-e STAGE_SERVER=${STAGE_SERVER} -e EXPOSE=${EXPOSE} -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -P prod -DskipTests clean package'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }
        stage('Deploy') {
            steps {
                sh 'mvn -B -P prod -DskipTests deploy'
            }
        }
        stage('Stage') {
            steps {
                // docker_tag
                sh 'echo -n idis/$(xmllint --xpath \'/*[local-name()="project"]/*[local-name()="artifactId"]/text()\' pom.xml): > docker_tag'
                sh 'echo -n $(xmllint --xpath \'/*[local-name()="project"]/*[local-name()="version"]/text()\' pom.xml) >> docker_tag'

                // server
                sh 'docker -H ${STAGE_SERVER} pull $(<docker_tag)'
                sh 'docker -H ${STAGE_SERVER} rm -f $(cut -d: -f1 docker_tag | tr / .) || true'
                sh 'docker -H ${STAGE_SERVER} network inspect jarvis || docker -H ${STAGE_SERVER} network create --subnet=172.20.0.0/16 jarvis'
                sh 'docker -H ${STAGE_SERVER} run -d --restart always \
                -e "JAVA_OPTION=${JAVA_OPTION}" \
                -e "IDIS_DATA_KAFKA_BOOTSTRAP_SERVERS=${IDIS_DATA_KAFKA_BOOTSTRAP_SERVERS}" \
                -e "IDIS_DATA_CHD_HOST=${IDIS_DATA_CHD_HOST}" \
                -e "IDIS_DATA_CHD_ADMIN_USERNAME=${IDIS_DATA_CHD_ADMIN_USERNAME}" \
                -e "IDIS_DATA_CHD_ADMIN_PASSWORD=${IDIS_DATA_CHD_ADMIN_PASSWORD}" \
                -e "IDIS_DATA_CDH_MYSQL_SERVER_HOST=${IDIS_DATA_CDH_MYSQL_SERVER_HOST}" \
                -e "IDIS_DATA_CDH_MYSQL_SERVER_PORT=${IDIS_DATA_CDH_MYSQL_SERVER_PORT}" \
                -e "IDIS_DATA_CDH_MYSQL_USERNAME=${IDIS_DATA_CDH_MYSQL_USERNAME}" \
                -e "IDIS_DATA_CDH_MYSQL_PASSWORD=${IDIS_DATA_CDH_MYSQL_PASSWORD}" \
                --net ${NETWORK} --ip ${IP_ADDRESS} --name $(cut -d: -f1 docker_tag | tr / .) $(<docker_tag)'
            }
        }
    }
}