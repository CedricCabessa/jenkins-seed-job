package com.genymobile.jenkins;

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.jobs.FreeStyleJob


class DockerBuilder {
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
    /**
     * branch to build (default origin/master)
     */
    String gitbranch = "origin/master"
    String dockername
    String pollScmSchedule = 'H/5 * * * *'
    int artefactToKeep = 10

    FreeStyleJob build(DslFactory dslFactory) {
        dslFactory.freeStyleJob(name) {
            it.description this.description
            logRotator {
                numToKeep artefactToKeep
            }
            scm {
                git {
                    remote {
                        url giturl
                        branch gitbranch
                        credentials("7bf7f643-0dea-46b4-a1fc-42387dd300e9") //fixme
                    }
                    clean()
                }

            }
            label dockername
            triggers {
                scm pollScmSchedule
            }
            if (buildScript) {
                steps {
                    shell buildScript
                }
            }
            if (archiveArtifacts) {
                publishers {
                    archiveArtifacts archiveArtifacts
                }
            }
        }
    }
}
