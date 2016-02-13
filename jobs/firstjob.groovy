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

job('myjob') {
    description("""A cool description""")
    scm {
        git("git://github.com/foo/bar")
    }
}
