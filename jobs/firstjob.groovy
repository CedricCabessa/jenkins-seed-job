import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

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

class JobBuilder {
    String name
    String description
    String giturl
    String gitBranch = 'master'
    String pollScmSchedule = 'H/5 * * * *'

    Job build(DslFactory dslFactory) {
        dslFactory.job(name) {
            it.description this.description
            logRotator {
                numToKeep 5
            }
            scm {
                git {
                    remote {
                        url(giturl)
                        branch(gitBranch)
                    }
                }
            }
            triggers {
                scm pollScmSchedule
            }
        }
    }
}

new JobBuilder(
        name: "myjobbuild job",
        description: "descri",
        giturl: "git@github:Plop",
).build(this)

job('myjob') {
    description("""A cool description""")
    scm {
        git("git://github.com/foo/bar")
    }
}


matrixJob("my multi job") {
    axes {
        label("label", "linnode", "winnode", "macnode")
    }
    scm {
        git {
            remote {
                url('git://github.com/CedricCabessa/lightduino')
                branch('${sha1}')
                refspec("+refs/pull/*:refs/remotes/origin/pr/*")
            }
        }
    }
    triggers {
        pullRequest {
            cron('H/5 * * * *')
            permitAll()
        }
    }
}
