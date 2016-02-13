import com.genymobile.jenkins.MasterMatrixBuilder;

job('DSL-Tutorial-1-Test') {
    description("""This a wonderful project
With a multiline description""")
    scm {
        git {
            remote {
                url('git://github.com/CedricCabessa/lightduino')
                branch('master')
            }
        }
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
        shell("""date > output
echo 'hello world'
""")
    }
    publishers {
        archiveArtifacts('output')
    }
}



new MasterMatrixBuilder(
        name: "mynew job",
        description: "awesome job",
        giturl: "http://giturl",
        buildScript: """
./build.sh
echo "plop"
"""
).build(this)
