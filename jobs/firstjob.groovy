job('DSL-Tutorial-1-Test') {
    description("""This a wonderful project
With a multiline description""")
    scm {
        git('git://github.com/CedricCabessa/lightduino')
    }
    triggers {
        scm('*/5 * * * *')
    }
    steps {
        shell("""date > output
echo 'hello world'
""")
    }
}