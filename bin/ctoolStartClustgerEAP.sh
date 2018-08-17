#!/bin/bash

#*************************************
# ctoolStartCluster.sh
# simple setup for starting a DSE cluster and installing from tarball or source
# Parameters:
#	cluster_name: name for cluster
#	cluster_size: how many nodes to include in the cluster
#	instance_type: type of openstack instance to use
#	dse_version: tarball version
#	build_source: source version
#	enable_graph: (Y|N) to enable graph
#	spark_flag: (0-1) 0-100 % of Spark nodes
#	search_flag: (0-1) 0-100 % of Search nodes
#	bashrc_flag: (Y|N) to load and source bashrc file
# Lorina Poland
#*************************************

read -p 'Cluster name: ' cluster_name
read -p 'Cluster size: ' cluster_size
read -p 'Cluster instance type (default is m3.large, type Y for default): ' instance_type
read -p 'DSE version (current is 6.0.0, type Y for current version): ' dse_version
read -p 'Build from source? Either N or supply branch name: ' build_source
read -p 'Enable graph? Y/N ' enable_graph
read -p 'Enable search analytics? 0-1 ' searchAnalytics_flag
read -p 'Enable spark? 0-1 ' spark_flag
read -p 'Enable search? 0-1 ' search_flag
#read -p 'DSA username: ' DSA_username
#read -p 'DSA password: ' DSA_password
read -p 'Bashrc file to upload? Y/N ' bashrc_flag

if [ $dse_version = "Y" ] || [ $dse_version = "y" ];
  then
    dse_version="6.0.0"
  else
    dse_version=$dse_version
fi

if [ $instance_type = "Y" ] || [ $instance_type = "y" ];
  then
    instance_type="--instance-type m3.large"
  else
    instance_type="--instance-type $instance_type"
fi

if [ $build_source = "N" ] || [ $build_source = "n" ];
  then
    build_source=""
  else
    build_source="-b $build_source"
fi

if [ $enable_graph = "Y" ] || [ $enable_graph = "y" ];
  then
    graph_flag="--enable-graph"
fi

if [ $spark_flag = "0" ];
  then
    spark_flag=""
  else
    spark_flag="-k $spark_flag"
fi

if [ $search_flag = "0" ];
  then
    search_flag=""
  else
    search_flag="-e $search_flag"
fi

if [ $searchAnalytics_flag = "1" ];
  then
    search_flag=""
    spark_flag=""
    searchAnalytics_flag="-g $searchAnalytics_flag --enable-dsefs"
fi

echo Launching cluster $cluster_name $cluster_size
ctool launch $cluster_name $cluster_size $instance_type &
wait

echo Installing DSE
    # install from a specified tarball
    echo Building from tarball with following parameters: $dse_version $graph_flag $spark_flag $search_flag $searchAnalytics_flag
    ctool install -v 6.0.0 -t https://alexander.gauthier%40datastax.com:Winner%4010@labs-downloads.datastax.com/ds-labs-20180125/tar/enterprise/ds-labs-20180125-dse.tar.gz -i tar $graph_flag $spark_flag $search_flag $searchAnalytics_flag $cluster_name enterprise &
wait

echo Modify dse.default based on parameters
if [[ $graph_flag ]]
  then
    ctool change_config $cluster_name all -f /home/automaton/dse/resources/dse/conf/dse.default -k "GRAPH_ENABLED=" -v "1"
fi
if [[ $search_flag ]]
  then
    ctool change_config $cluster_name all -f /home/automaton/dse/resources/dse/conf/dse.default -k "SOLR_ENABLED=" -v "1"
fi
if [[ $spark_flag ]]
  then
    ctool change_config $cluster_name all -f /home/automaton/dse/resources/dse/conf/dse.default -k "SPARK_ENABLED=" -v "1"
fi

echo Starting cluster
ctool start $cluster_name enterprise &
wait

ctool run $cluster_name 0 "dsetool status"
echo "Done - cluster provisioned and running"

if [ $bashrc_flag = "Y" ] || [ $bashrc_flag = "y" ];
  then
    ctool run $cluster_name 0 "git clone https://github.com/magicmonty/bash-git-prompt.git .bash-git-prompt --depth=1"
    ctool scp $cluster_name 0 $HOME/.git-prompt-colors.sh /home/automaton/.git-prompt-colors.sh
    ctool scp $cluster_name 0 $HOME/.bashrc /home/automaton/.bashrc
    ctool run $cluster_name 0 "source /home/automaton/.bashrc"
fi
