/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.ps.server

import scala.collection.mutable.HashMap

/**
 * Created by genmao.ygm on 15-3-18.
 */
class PSServer[K, V](serverId: Long, func: (V, V) => V) {

  val keyValue: HashMap[K, V] = new HashMap[K, V]()
  // 暂时没想怎么定义这个
  val inValidV = -1.asInstanceOf[V]

  def getParameter(key: K): V = {
    keyValue.getOrElse(key, inValidV)
  }

  def setParameter(key: K, value: V): Unit = {
    keyValue.synchronized {
      keyValue(key) = value
    }
  }

  def updateParameter(key: K, value: V): Unit = {
    keyValue.synchronized {
      if(keyValue.contains(key)) {
        val oldV = keyValue.get(key).get
        val v = func(value, oldV)
        keyValue(key) = v
      } else {
        keyValue(key) = value
      }
    }
  }

}
