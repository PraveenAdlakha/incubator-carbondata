/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.carbondata.spark.testsuite.filterexpr

import org.apache.spark.sql.Row
import org.apache.spark.sql.common.util.CarbonHiveContext._
import org.apache.spark.sql.common.util.QueryTest
import org.carbondata.core.constants.CarbonCommonConstants
import org.carbondata.core.util.CarbonProperties
import org.scalatest.BeforeAndAfterAll

class NullMeasureValueTestCaseFilter extends QueryTest with BeforeAndAfterAll {

  override def beforeAll {
    sql("drop table if exists t3")
    sql(
      "CREATE TABLE t3 (ID bigInt, date Timestamp, country String, name String, " +
        "phonetype String, serialname String, salary Int) STORED BY 'org.apache.carbondata.format'"
    )
    CarbonProperties.getInstance()
      .addProperty(CarbonCommonConstants.CARBON_TIMESTAMP_FORMAT, "yyyy/mm/dd")
    sql("LOAD DATA LOCAL INPATH './src/test/resources/datawithnullmeasure.csv' into table t3");
  }

  test("select ID from t3 where salary is not null") {
    checkAnswer(
      sql("select ID from t3 where salary is not null"),
      Seq(Row(1),Row(4)))
  }

  test("select ID from t3 where salary is null") {
    checkAnswer(
      sql("select ID from t3 where salary is null"),
      Seq(Row(2),Row(3)))
  }

  override def afterAll {
    sql("drop table t3")
    CarbonProperties.getInstance()
      .addProperty(CarbonCommonConstants.CARBON_TIMESTAMP_FORMAT, "dd-MM-yyyy")
  }
}
