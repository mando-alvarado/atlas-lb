#!/usr/bin/env python

""" Server to experiment with persistent connections clients"""

from threading import Thread
from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
from SocketServer import ForkingMixIn, ThreadingMixIn
import copy
import thread
import time
import sys
import os

def usage(prog):
    printf("usage is %s <host> <port>\n",prog)

def printf(format,*args): sys.stdout.write(format%args)

def fprintf(fp,format,*args): fp.write(format%args)

class ThreadedHTTPServer(ThreadingMixIn,HTTPServer):
    pass

class TestHandler(BaseHTTPRequestHandler):
    protocol_version = "HTTP/1.1"
    jsonData = """{"data":"json"}"""
    xmlData = """<data>json</data>"""
    lck = thread.allocate_lock()
    def __init__(self,*args,**kw):
        BaseHTTPRequestHandler.__init__(self,*args,**kw)

    def lock(self):
        TestHandler.lck.acquire()

    def unlock(self):
        TestHandler.lck.release()

    def do_POST(self):
        cl = int(self.headers.getheader("content-length"))
        eh = self.headers.getheader("body","accept")
        body = self.rfile.read(cl)
        if self.path.lower() == "/json":
            self.lock()
            TestHandler.jsonData = body
            self.unlock()
            self.send_response(200)
            self.send_header("content-length",0)
            self.end_headers()
            self.wfile.write("")
            return
        if self.path.lower() == "/xml":
            self.lock()
            TestHandler.xmlData = body
            self.unlock()
            self.send_response(200)
            self.send_header("content-length",0)
            self.end_headers()
            self.wfile.write("")
            return
        if eh == "accept":
            (data,ct) = self.getContent()
        elif eh == "echo":
            data = body
            ct = self.headers.getheader("content-type")
        else:
            data = ""
            ct = "application/xml"
        cl = "%s"%len(data)
        printf("cl = %s\n",cl)
        self.send_response(200)
        self.send_header("content-length",cl)
        self.send_header("content-type",ct)
        self.end_headers()
        self.wfile.write(data)
        return       

    def getContent(self):
        ah  = self.headers.getheader("accept","application/json").lower()
        ats = set([t.split(";")[0].strip() for t in ah.split(",")])
        if "application/json" in ats:
            self.lock()
            data = copy.copy(TestHandler.jsonData)
            self.unlock()
            ct = "application/json"
            return (data,ct)
        elif "application/xml":
            self.lock()
            data = copy.copy(TestHandler.xmlData)
            self.unlock()
            ct = "application/xml"
            return (data,ct)

    def do_GET(self):
        (data,ct) = self.getContent()
        self.send_response(200)
        self.send_header("Content-Length",len(data))
        self.end_headers()
        self.wfile.write(data)
        return

if __name__ == '__main__':
    prog = os.path.basename(sys.argv[0])
    if len(sys.argv)<3:
        usage(prog)
        sys.exit()
    host = sys.argv[1]
    port = int(sys.argv[2])
    server = ThreadedHTTPServer((host,port),TestHandler)
    server.serve_forever()
    
    

