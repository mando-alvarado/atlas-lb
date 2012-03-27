#!/usr/bin/env jython

from util import *
import util
import sys
import os

token = load_json("auth_resp.json")["token"]
conf  = load_json("dns.json")
ep = conf["endpoint"]
aid = conf["account"]

dns = DnsClient1_0(aid,ep,token)

resp = dns.getDomains(None,None,None)

domains = [(d.getId(),d.getName()) for d in resp.getDomain()]
print domains
