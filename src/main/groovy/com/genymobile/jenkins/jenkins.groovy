package com.genymobile.jenkins;

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.jobs.MatrixJob


class MasterMatrixBuilder {
    String name
    String description
    String giturl
    String buildScript
    String archiveArtifacts
    String gitBranch = 'master'
    String pollScmSchedule = 'H/5 * * * *'
    int artefactToKeep = 10

    MatrixJob build(DslFactory dslFactory) {
        dslFactory.matrixJob(name) {
            it.description this.description
            logRotator {
                numToKeep artefactToKeep
            }
            scm {
                git {
                    remote {
                        url(giturl)
                        branch(gitBranch)
                        credentials("user")
                    }
                    clean()
                }

            }
            triggers {
                scm pollScmSchedule
            }
            axes {
                label("label", "linnode", "winnode", "macnode")
            }
            steps {
                shell(buildScript)
            }
            publishers {
                archiveArtifacts(archiveArtifacts)
            }
        }
    }
}