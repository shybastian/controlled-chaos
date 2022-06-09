module.exports = function(grunt) {
  // Project configuration.
  grunt.initConfig({
    ebDeploy: {
      options: {
        region: 'eu-central-1',
        application: 'controlled-front',
      },
      dev: {
        options: {
          environment: 'controlled-front'
        },
        files: [
          { src: ['.ebextensions/*'] },
          { cwd: 'dist/', src: ['**'], expand: true }
        ]
      },
      prod: {
        options: {
          environment: 'controlled-front-prod'
        },
        files: [
          { src: ['.ebextensions/*'] },
          { cwd: 'dist/', src: ['**'], expand: true }
        ]
      },
    },
  });
  grunt.loadNpmTasks('grunt-eb-deploy');
};
