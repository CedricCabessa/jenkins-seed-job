package genymotion

import com.genymobile.jenkins.DockerBuilder;


def job = new DockerBuilder(
        name: "nombirl",
        dockername: "android_docker",
        giturl: "git@github.com:Genymobile/genymotion-testing.git",
        archiveArtifacts: "Nombril/nombril/build/outputs/apk/*.apk"
).build(this);

job.steps {
    gradle {
        rootBuildScriptDir "Nombril"
        tasks "assembleDebug"
    }
}