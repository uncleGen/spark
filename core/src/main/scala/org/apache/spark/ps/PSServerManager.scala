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

package org.apache.spark.ps

import scala.collection.mutable.HashMap
import org.apache.hadoop.yarn.api.records.ContainerId
import akka.actor.ActorRef
import org.omg.PortableInterceptor.ServerIdHelper
import java.util.concurrent.atomic.AtomicLong
import scala.collection.mutable

/**
 * Created by genmao.ygm on 15-3-23.
 */
class PSServerManager {

  val nextServerId = new AtomicLong(0)

  val executorId2Server = new HashMap[String, ServerData]()
  val executorId2ServerId = new HashMap[String, Long]()

  def addPSServer(executorId: String, hostPort: String, serverData: ServerData) {
    executorId2Server(executorId) = serverData
    executorId2ServerId(executorId) = serverData.serverId
  }

  def newServerId(): Long = nextServerId.getAndIncrement

}
