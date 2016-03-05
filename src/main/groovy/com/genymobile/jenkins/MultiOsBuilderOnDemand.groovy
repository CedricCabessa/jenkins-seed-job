package com.genymobile.jenkins;

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.jobs.MatrixJob

class MultiOsBuilderOnDemand {
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
                        url giturl
                        branch '$branch'
                        credentials "7bf7f643-0dea-46b4-a1fc-42387dd300e9" //fixme
                    }
                    clean()
                }

            }
            parameters {
                stringParam("branch", "origin/master", "Branch to build")
            }
            axes {
                //fixme:
                label("label", "linnode", "winnode", "macnode")
            }
            steps {
                shell buildScript
            }
            if (archiveArtifacts) {
                publishers {
                    archiveArtifacts archiveArtifacts
                }
            }
        }
    }
}