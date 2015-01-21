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

package org.apache.spark;

import java.io.Serializable;

import scala.Function0;
import scala.Function1;
import scala.Unit;

import org.apache.spark.annotation.DeveloperApi;
import org.apache.spark.executor.TaskMetrics;
import org.apache.spark.util.TaskCompletionListener;
import org.apache.spark.util.TaskKilledListener;

/**
 * Contextual information about a task which can be read or mutated during
 * execution. To access the TaskContext for a running task use
 * TaskContext.get().
 */
public abstract class TaskContext implements Serializable {
  /**
   * Return the currently active TaskContext. This can be called inside of
   * user functions to access contextual information about running tasks.
   */
  public static TaskContext get() {
    return taskContext.get();
  }

  private static ThreadLocal<TaskContext> taskContext =
    new ThreadLocal<TaskContext>();

  static void setTaskContext(TaskContext tc) {
    taskContext.set(tc);
  }

  static void unset() {
    taskContext.remove();
  }

  /**
   * Whether the task has completed.
   */
  public abstract boolean isCompleted();

  /**
   * Whether the task has been killed.
   */
  public abstract boolean isInterrupted();

  /** @deprecated use {@link #isRunningLocally()} */
  @Deprecated
  public abstract boolean runningLocally();

  public abstract boolean isRunningLocally();

  /**
   * Add a (Java friendly) listener to be executed on task completion.
   * This will be called in all situation - success, failure, or cancellation.
   * An example use is for HadoopRDD to register a callback to close the input stream.
   */
  public abstract TaskContext addTaskCompletionListener(TaskCompletionListener listener);

  /**
   * Add a listener in the form of a Scala closure to be executed on task completion.
   * This will be called in all situations - success, failure, or cancellation.
   * An example use is for HadoopRDD to register a callback to close the input stream.
   */
  public abstract TaskContext addTaskCompletionListener(final Function1<TaskContext, Unit> f);

  /**
   * Add a callback function to be executed on task completion. An example use
   * is for HadoopRDD to register a callback to close the input stream.
   * Will be called in any situation - success, failure, or cancellation.
   *
   * @deprecated use {@link #addTaskCompletionListener(scala.Function1)}
   *
   * @param f Callback function.
   */
  @Deprecated
  public abstract void addOnCompleteCallback(final Function0<Unit> f);

  /**
   * Add a (Java friendly) listener to be executed on task interruption. We add this
   * listener for some more clean works. An example use is to stop `receiver supervisor`
   * properly.
   */
  public abstract TaskContext addTaskKilledListener(TaskKilledListener listener);

  /**
   * Add a listener in the form of a Scala closure to be executed on task interruption.
   * We add this listener for some more clean works. An example use is stop `receiver
   * supervisor` properly.
   */
  public abstract TaskContext addTaskKilledListener(final Function1<TaskContext, Unit> f);

  /**
   * The ID of the stage that this task belong to.
   */
  public abstract int stageId();

  /**
   * The ID of the RDD partition that is computed by this task.
   */
  public abstract int partitionId();

  /**
   * How many times this task has been attempted.  The first task attempt will be assigned
   * attemptNumber = 0, and subsequent attempts will have increasing attempt numbers.
   */
  public abstract int attemptNumber();

  /** @deprecated use {@link #taskAttemptId()}; it was renamed to avoid ambiguity. */
  @Deprecated
  public abstract long attemptId();

  /**
   * An ID that is unique to this task attempt (within the same SparkContext, no two task attempts
   * will share the same attempt ID).  This is roughly equivalent to Hadoop's TaskAttemptID.
   */
  public abstract long taskAttemptId();

  /** ::DeveloperApi:: */
  @DeveloperApi
  public abstract TaskMetrics taskMetrics();
}
