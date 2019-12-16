import org.sonatype.nexus.security.user.UserManager
import org.sonatype.nexus.security.role.NoSuchRoleException

try {
  security.securitySystem.getAuthorizationManager(UserManager.DEFAULT_SOURCE).getRole('snapshot-upload')
}
catch (NoSuchRoleException ex) {
  // Only add it if it isn't already there
  security.addRole('snapshot-upload', 'snapshot-upload', 'Ability to upload to snapshots', ['nx-repository-view-maven2-libs-snapshot-*'], ['nx-anonymous'])
}
