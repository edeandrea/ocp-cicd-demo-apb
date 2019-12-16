import org.sonatype.nexus.security.user.UserNotFoundException

try {
  security.securitySystem.getUser('snapshot')
}
catch (UserNotFoundException ex) {
  // Only add it if it isn't already there
  security.addUser('snapshot', 'Snapshot', 'User', 'edeandrea@redhat.com', true, 'snapshot', ['snapshot-upload', 'nx-anonymous'])
}
