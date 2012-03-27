#!/usr/bin/env python

import dns.dns as objs
import urlparse
import httplib
import urllib
import base64
import timer
import time
import sys
import os



def printf(format,*args): sys.stdout.write(format%args)

def fprintf(fp,format,*args): fp.write(format%args)

def basicAuth(user,passwd):
    basicAuth64 = base64.b64encode("%s:%s"%(user,passwd))
    hdr = {"basic" : basicAuth64}
    return hdr

def decodeUrl(strIn):
    hdr = {}
    u = urlparse.urlparse(strIn)
    prot = u.scheme
    netlocSplit = u.netloc.split("@")
    if len(netlocSplit)>=2:
        (user,passwd) = netlocSplit[0].split(":")
        hdr.update(basicAuth(user,passwd))
        hp = netlocSplit[1].split(":")
    else:
        hp = netlocSplit[0].split(":")
    if len(hp)>=2:
        host = hp[0]
        port = int(hp[1])
    else:
        host = hp[0]
        port = 80
    uri = u.path
    qp = u.query
    frag = u.fragment    
    return (prot,host,port,uri,qp,frag,hdr)

def buildDnsFault(message,details):
    df = objs.dnsFault()
    df.message = message
    df.details = details
    return df.toxml()
    
def usage(prog):
    printf("usage is %s <url> <n> <accept>\n",prog)
    printf("\n")
    printf("Sends n requests persistently\n")

if __name__ == "__main__":
    t = timer.Timer()
    t.start()    
    prog = os.path.basename(sys.argv[0])
    if len(sys.argv)<4:
        usage(prog)
        sys.exit()
    url = sys.argv[1]
    n = int(sys.argv[2])
    accept = sys.argv[3]

    (prot,host,port,uri,qp,frag,hdrs) = decodeUrl(url)

    if prot == "http":
        conClass = httplib.HTTPConnection
    elif prot == "https":
        conClass = httplib.HTTPSConnection
    else:
        printf("Unknown protocol scheme %s\n",prot)

    hdrs["body"]="echo"
    hdrs["accept"] = accept

    t.reset()
    reqData = []
    for i in xrange(0,n):
        reqData.append(buildDnsFault("Post[%i]"%i,"Persistent test"))

    printf("Took %.3f seconds to build objects\n",t.read_reset())

    respList = []

    for i in xrange(0,n):
        con = conClass(host,port)
        con.request("POST",uri,reqData[i],hdrs)
        resp = con.getresponse()
        printf("req[%i][\"body\"]=%s\n",i,resp.read())
        printf("req[%i][\status\"]=%s\n",i,resp.status)
        con.close()
    printf("Took %.3f seconds\n",t.read_reset())
