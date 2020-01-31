# ocp-ci-cd-demo-apb
OpenShift CI/CD Demo provisioning playbook on an OCP 4.x cluster.

The playbook provisions all of the necessary pieces to run the demo (CodeReady Workspaces, SonarQube, Nexus, Jenkins). It also provisions the Red Hat Application Migration Toolkit & the [OpenShift Cluster Logging](https://docs.openshift.com/container-platform/4.2/logging/cluster-logging-deploying.html).

## Deployed Resource URLs
All the below resource URLs are suffixed with the apps url of the cluster (i.e. for an RHPDS environment, `apps.cluster-##GUID##.##GUID##.example.opentlc.com`).

- [Nexus OSS](https://www.sonatype.com/nexus-repository-oss)
    - https://nexus-labs-infra.##CLUSTER_SUFFIX##
    - Admin user/pwd is `admin/admin123`
- [SonarQube](https://www.sonarqube.org)
    - https://sonarqube-labs-infra.##CLUSTER_SUFFIX##
    - Admin user/pwd is `admin/admin`
- [CodeReady Workspaces 2.0](https://developers.redhat.com/products/codeready-workspaces/overview)
    - http://codeready-labs-infra.##CLUSTER_SUFFIX##
- [Red Hat Application Migration Toolkit](https://developers.redhat.com/products/rhamt/overview)
    - http://rhamt-web-console-labs-infra.##CLUSTER_SUFFIX##
- [Kibana](https://www.elastic.co/kibana) (Cluster Logging Dashboard)
    - https://kibana-openshift-logging.##CLUSTER_SUFFIX##
- [Grafana](https://grafana.com/grafana) (Cluster Monitoring Dashboard)
    - https://grafana-openshift-monitoring.##CLUSTER_SUFFIX##
- [Demo app](https://github.com/edeandrea/summit-lab-spring-music/tree/pipeline) in dev
    - Main page
        - http://spring-music-dev.##CLUSTER_SUFFIX##
    - Actuator home
        - http://spring-music-dev.##CLUSTER_SUFFIX##/actuator
    - Swagger UI
        - http://spring-music-dev.##CLUSTER_SUFFIX##/swagger-ui.html
- [Demo app](https://github.com/edeandrea/summit-lab-spring-music/tree/pipeline) in prod
    - Main page
        - http://spring-music-prod.##CLUSTER_SUFFIX##
    - Actuator home
        - http://spring-music-prod.##CLUSTER_SUFFIX##/actuator
    - Swagger UI
        - http://spring-music-prod.##CLUSTER_SUFFIX##/swagger-ui.html
- [Demo app](https://github.com/edeandrea/summit-lab-spring-music/tree/pipeline) image on quay.io
    - https://quay.io/repository/edeandrea/spring-music?tab=tags

## Demo Pipeline Stages
The demo takes a [Spring Boot Application](https://github.com/edeandrea/summit-lab-spring-music/tree/pipeline) and deploys it to OpenShift along with a pipeline that moves it through various stages:

- Build app
    - Builds the application binary (a Spring Boot fat jar)
- Unit Test
    - Runs all unit tests
- Sonar Scan
    - Performs a sonar analysis
- Publish Artifact
    - Pushes the fat jar to the Nexus repository
- Build Image
    - Builds a container image for the app using S2I Binary build
- Deploy
    - Deploys the image to dev using a rolling deployment
- Integration Test
    - Runs a simple integration test against the running app
- Push to Quay - Container Image Scan
    - Pushes the image to Quay and runs security scan
    - Right now its configured so the pipeline manually triggers the push, but it could be configured so the pipeline itself didn’t need to trigger it, it would be triggered automatically by OpenShift on the Build Image phase
        - It is just a separate build config that is set up to push the image from OCP’s internal repo to Quay. Currently that build config is not set up to trigger automatically upon changes to the source image
- Promote to Prod
    - Requires user input as to whether to move to prod or not
    - If yes, will re-tag the dev image as prod and move to the prod project's image repo
- Deploy to prod
    - Stands up the app in prod using a rolling deployment
        - If the first time, will create a new app and do all of the bindings necessary to hook it into the PostgreSQL database
    - If not the first time, really doesn't do anything, as the pushing of a new image will trigger a new deployment of the image
    - Force that there are 2 instances of the app running

 To run this you would do something like
 ```bash
$ ansible-playbook -vvv main.yml -e ocp_api_url=<OCP_API_URL> -e ocp_admin_pwd=<OCP_ADMIN_USER_PASSWORD> -e quay_docker_config_json_file=<LOCATION_OF_QUAY_SECRET>
 ```

You'll need to replace `<OCP_API_URL>` with the API url of your cluster, `<OCP_ADMIN_USER_PASSWORD>` with the password for the OCP admin account, & `LOCATION_OF_QUAY_SECRET` with the path to the Quay secret json file.

This playbook also makes some assumptions about some things within the cluster. These variables can be overridden with the `-e` switch when running the playbook.

| Description | Variable | Default Value |
| ----------- | -------- | ------------- |
| OpenShift admin user name | `ocp_admin` | `opentlc-mgr` |
| OCP Project containing operators | `proj_nm_operators` | `gpte-operators` |
| OCP user to install demo into | `ocp_proj_user` | `user1` |
| OCP user password for above user | `ocp_proj_user_pwd` | `openshift` |
