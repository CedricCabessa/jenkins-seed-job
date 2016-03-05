package genymotion

import com.genymobile.jenkins.MultiOsBuilder;

def genymotion_soft_master_description =
        '''Takes care of building genymotion soft master on changes
This configuration is for Linux, Mac OSX and Windows
'''

def genymotion_soft_master_script =
        '''# Git sync
git submodule sync
git submodule update --init --recursive --force

# OS_TYPE is set in each node configuration
jenkins/build -p -t $OS_TYPE
if [ "$OS_TYPE" = "linux64" ]; then
    if [ -e "jenkins/run_cppcheck" ] ; then
        # Remove this if when all branches have jenkins/run_cppcheck
        jenkins/run_cppcheck -i build --xml 2> cppcheck.xml
    else
        cppcheck -i build/ -i libs/librendering/qemu -i libs/libvmdeploy/lib/vbox/4.3 -i libs/libvmdeploy/lib/vbox/5.0 --enable=all --inconclusive --xml --xml-version=2 . 2> cppcheck.xml
    fi
fi
'''

def job = new MultiOsBuilder(
        name: "genymotion-soft-master-groovy",
        description: genymotion_soft_master_description,
        giturl: "git@github.com:Genymobile/genymotion-softs.git",
        buildScript:genymotion_soft_master_script,
        archiveArtifacts: "build/*.bin,build/*.dmg,build/genymotion-*.exe"
).build(this);

job.publishers {
    archiveXUnit {
        qTestLib {
            pattern("**/result.xml")
            skipNoTestFiles true
            deleteOutputFiles false
            failedThresholds {
                failure 1
            }
        }
    }
}
