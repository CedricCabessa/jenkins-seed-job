import com.genymobile.jenkins.MultiOsBuilder;

def genymotion_soft_on_demand_description = """
On-demand dedicated job. You can build with both parameters: branch and target (regular|MVP)
This configuration is for Linux, Mac OSX and Windows
"""
def genymotion_soft_on_demand_script = """
# Git sync
git submodule sync
git submodule update --init --recursive --force

# Setup target
if [ "$TARGET" = "MVP" ]; then
    build_options="-q CONFIG+=share_feature -q CONFIG+=use_token_test_key"
fi

# OS_TYPE is set in each node configuration
jenkins/build -p -t $OS_TYPE $build_options

if [ "$OS_TYPE" = "linux64" ]; then
    if [ -e "jenkins/run_cppcheck" ] ; then
        # Remove this if when all branches have jenkins/run_cppcheck
        jenkins/run_cppcheck -i build --xml 2> cppcheck.xml
    else
        cppcheck -i build/ -i libs/librendering/qemu -i libs/libvmdeploy/lib/vbox/4.3 -i libs/libvmdeploy/lib/vbox/5.0 --enable=all --inconclusive --xml --xml-version=2 . 2> cppcheck.xml
    fi
fi
"""

def job = new MultiOsBuilder(
        name: "genymotion-soft-on-demand-groovy",
        description: genymotion_soft_on_demand_description,
        giturl: "git@github.com:Genymobile/genymotion-build.git",
        buildScript:genymotion_soft_on_demand_script,
        archiveArtifacts: "build/*.bin,build/*.dmg,build/genymotion-*.exe"
);

def matrix = job.build(this);

