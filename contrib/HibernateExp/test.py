#!/usr/bin/env jython
	
import util
util.setConfig("slice.json")
from util import *

import java.util.regex.Pattern as Pattern
import java.util.regex.Matcher as Matcher

import org.hexp.hibernateexp.util.RegTest as RegTest

stubs.ce.getFileNames()
String(stubs.ce.downloadFile("global_error.html"))


vnames = stubs.vs.getVirtualServerNames()
protocols = stubs.vs.getProtocol(vnames)

for i in xrange(0,len(vnames)):
    if protocols[i].toString() != "http":
       continue
    print vnames[]

httpvs = [v for (v,p) in zip(vnames,protocols) if p.toString()=="http"]
errorfiles = ["global_error.html"]*len(httpvs)

stubs.vs.setErrorFile(httpvs, errorfiles)


pattern_str = r"<([A-Z][A-Z0-9]*)\b[^>]*>(.*?)</\1>"


pattern_str = r".*"
p = Pattern.compile(pattern_str)


mn = "354934_42"
stubs.m.addMonitors([mn])
stubs.m.setType([mn],[CatalogMonitorType.http])
stubs.m.setStatusRegex([mn],[r".*"])
stubs.m.setPath([mn],[r"/blah"])
stubs.m.setBodyRegex([mn],[pattern_str])
#borkage above
stubs.p.setMonitors([mn],[[mn]])

