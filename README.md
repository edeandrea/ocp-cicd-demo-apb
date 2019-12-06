# ocp-ci-cd-demo-apb
 OpenShift CI/CD Demo provisioning playbook on an OCP 4.x cluster

 To run this you would do something like
 ```bash
$ ansible-playbook -vvv main.yml -e ocp_api_url=<OCP_API_URL> -e ocp_admin_pwd=<OCP_ADMIN_USER_PASSWORD>
 ```

You'll need to replace `<OCP_API_URL>` with the API url of your cluster & `<OCP_ADMIN_USER_PASSWORD>` with the password for the OCP admin account.

This playbook also makes some assumptions about some things within the cluster. These variables can be overridden with the `-e` switch when running the playbook.

| Description | Variable | Default Value |
| ----------- | -------- | ------------- |
| OpenShift admin user name | `ocp_admin` | `opentlc-mgr` |
| OCP Project containing operators | `proj_nm_operators` | `gpte-operators` |
| OCP Project containing infra components (Nexus, SonarQube, etc) | `proj_nm_infra` | `labs-infra` |
| OCP user to install demo into | `ocp_proj_user` | `user1` |
| OCP user password for above user | `ocp_proj_user_pwd` | `openshift` |
