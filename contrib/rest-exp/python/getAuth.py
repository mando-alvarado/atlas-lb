#!/usr/bin/env jython

from util import *
import util
import sys
import os

def usage(prog):
    printf("usage is %s <auth.json>\n",prog)
    printf("\n")
    printf("    fetch and display auth credentials\n")



prog = os.path.basename(sys.argv[0])
if len(sys.argv)<2:
    usage(prog)
    sys.exit()

#conf_file = sys.argv[1]
conf_file = "cgarza.json"


conf = load_json(conf_file)

auth = Auth1_1Client(conf["endpoint"],conf["user"],conf["key"])

resp = auth.getAuthResponse()

obj = {"token":resp.getToken().getId()}
save_json("auth_resp.json",obj)
