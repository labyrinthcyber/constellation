/*
 * Copyright 2010-2022 Australian Signals Directorate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.gov.tac.constellation.views.mapview2.utillities;

/**
 *
 * @author altair1673
 */
public class Vec3 {
    public double x = 0;
    public double y = 0;
    public double z = 0;

    public Vec3(Vec3 vec3) {
        x = vec3.x;
        y = vec3.y;
        z = vec3.z;
    }

    public void multiplyFloat(float value) {
        x *= value;
        y *= value;
        z *= value;
    }

    public void multiplyDouble(double value) {
        x *= value;
        y *= value;
        z *= value;
    }

    public void addVector(Vec3 value) {
        x += value.x;
        y += value.y;
        z += value.z;
    }

    public Vec3() {

    }

}
