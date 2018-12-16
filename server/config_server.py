import os
import sys

project_dir = os.path.split(os.path.abspath(os.path.dirname(__file__)))[:-1][0]
sys.path.append(project_dir)

import config_global

config = config_global
