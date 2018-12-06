from ipdb import set_trace

import os
import sys
import time

path_str = "C:/projects/feup-aiad/scripts"

start = time.time()


java_path = 'java'

# memory_flags = "-Xms128m -Xmx4g"

memory_flags = ""

# java_agent = "-javaagent:" \
#              "C:\\Users\\xfont\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\182.4892.20\lib\idea_rt.jar=65237" \
#              ":C:\\Users\\xfont\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\182.4892.20\bin -Dfile.encoding=UTF-8"

java_agent = ""

classpath = "-classpath " \
            "\"" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\charsets.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\deploy.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\access-bridge-64.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\cldrdata.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\dnsns.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\jaccess.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\jfxrt.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\localedata.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\nashorn.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\sunec.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\sunjce_provider.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\sunmscapi.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\sunpkcs11.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\zipfs.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\javaws.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\jce.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\jfr.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\jfxswt.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\jsse.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\management-agent.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\plugin.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\resources.jar;" \
            "C:\Program Files\Java\jdk1.8.0_171\jre\lib\rt.jar;" \
            "C:\projects\feup-aiad\out\production\feup-aiad;" \
            "C:\projects\feup-aiad\libs\jade\lib\jade.jar;" \
            "C:\projects\feup-aiad\libs\repastj\repast.jar;" \
            "C:\projects\feup-aiad\libs\sajas\lib\SAJaS.jar" \
            "\""

# classpath = "-classpath C:\projects\\feup-aiad\out\production\\feup-aiad"

launcher = "C:\projects\\feup-aiad\out\production\\feup-aiad\launchers\EnergyMarketLauncher"

launcher = "launchers.EnergyMarketLauncher"

params = "teste.txt 10 10 10 0.5 0.5"

execute_string = "" + java_path + " " + \
                 memory_flags  + " " + \
                 java_agent + " " + \
                 classpath + " " + \
                 launcher + " " + \
                 params

set_trace()

os.chdir(path_str)
os.system(execute_string)

print('It took %.2f minutes.' % ((time.time()-start)/60.0))