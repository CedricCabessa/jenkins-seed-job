package com.genymobile.jenkins;

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.jobs.MatrixJob


class MultiOsBuilder {
    /**
     * Job name
     */
    String name
    /**
     * job description
     */
    String description
    /**
     * git url (eg: git@github:Genymobile/genymotion-softs)
     */
    String giturl
    /**
     * script used for build. You can use multiline string with '''...'''
     */
    String buildScript
    /**
     * Path to artefact to archive
     */
    String archiveArtifacts
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
                        branch('$branch')
                        credentials("user") //fixme
                    }
                    clean()
                }

            }
            triggers {
                scm pollScmSchedule
            }
            parameters {
                stringParam("branch", "master", "Branch to build")
            }
            axes {
                 //fixme:
                label("label", "linnode", "winnode", "macnode")
            }
            steps {
                shell(buildScript)
            }
            if (archiveArtifacts) {
                publishers {
                    archiveArtifacts(archiveArtifacts)
                }
            }
        }
    }
}