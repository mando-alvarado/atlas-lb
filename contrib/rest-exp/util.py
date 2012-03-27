#!/usr/bin/env jython

import org.openstack.atlas.restclients.auth.Auth1_1Client as Auth1_1Client
import org.openstack.atlas.restclients.dns.DnsClient1_0 as DnsClient1_0
import org.openstack.atlas.restclients.dns.TestClient as TestClient
import org.openstack.atlas.restclients.dns.objects as objs

import com.xhaus.jyson.JysonCodec as json
import sys
import os

def printf(format,*args): sys.stdout.write(format%args)

def fullPath(file_path):
    return os.path.expanduser(file_path)

def save_json(file_path,obj):
    full_path = fullPath(file_path)
    jsonStr = json.dumps(obj)
    fp = open(full_path,"w")
    fp.write(jsonStr)
    fp.close()

def load_json(file_path):
    full_path = fullPath(file_path)
    fp = open(full_path,"r")
    jsonStr = fp.read()
    fp.close()
    obj = json.loads(jsonStr)
    return obj

