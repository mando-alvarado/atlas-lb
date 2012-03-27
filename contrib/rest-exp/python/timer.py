#!/usr/bin/env python

import time

class Timer(object):
    def __init__(self):
        self.begin   = time.time()
        self.end   = time.time()
        self.stored  = 0.0
        self.stopped = True

    def restart(self):
        self.reset()
        self.start()


    def start(self):
        if not self.stopped:
            return
        self.begin = time.time()
        self.stopped = False

    def stop(self):
        if self.stopped:
            return
        self.end = time.time()
        self.stored += self.end - self.begin
        self.stopped = True

    def read(self):
        if self.stopped:
             return self.stored
        now = time.time()
        total_time = now - self.begin + self.stored
        return total_time

    def reset(self):
        self.begin = time.time()
        self.end   = time.time()
        self.stored = 0.0

    def read_reset(self):
        if self.stopped:
             return self.stored
        now = time.time()
        out = now - self.begin + self.stored
        self.begin = time.time()
        self.end   = time.time()
        self.stored = 0.0
        return out
