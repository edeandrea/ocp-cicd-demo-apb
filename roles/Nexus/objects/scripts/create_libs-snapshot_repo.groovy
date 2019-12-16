import org.sonatype.nexus.repository.config.Configuration

configuration = new Configuration(
  repositoryName: 'libs-snapshot',
  recipeName: 'maven2-hosted',
  online: true,
  attributes: [
    maven: [
      versionPolicy: 'MIXED',
      layoutPolicy : 'PERMISSIVE'
    ],
    storage: [
      writePolicy: 'ALLOW',
      blobStoreName: 'default',
      strictContentTypeValidation: true
    ]
  ]
)

def existingRepository = repository.getRepositoryManager().get('libs-snapshot')

if (existingRepository != null) {
  existingRepository.stop()
  configuration.attributes['storage']['blobStoreName'] = existingRepository.configuration.attributes['storage']['blobStoreName']
  existingRepository.update(configuration)
  existingRepository.start()
}
else {
  repository.getRepositoryManager().create(configuration)
}
