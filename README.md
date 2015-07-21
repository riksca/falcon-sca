# falcon-sca
Source code for the Card Marshal application.

## Branches
The default branch is the production tree.  Development is used to merge changes in for tested before merging to the Production branch.  
To work on a feature, checkout the develop branch and branch that for your feature or fix.  Once it's tested and ready for integration testing, merge it back with develop. After that is tested, merge with the master branch for production deployment. (*this is subject to change)

## Running in dev
from falcon-ear, run gradle appengineRun
Do not run this from the top live falcon-sca level.

You can run the admin module separately.
