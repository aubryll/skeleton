/*
 *   Spring boot skeleton©
 *    Copyright 2020 Acclamenia Ltd
 *     Url: https://github.com/aubryll/skeleton
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.acclamenia.service.cache;
import org.springframework.data.util.Pair;
import java.util.List;

public interface IBaseConfigCacheService<T> {

    void add(Pair<String, T> pair);

    void remove(Pair<String, T> pair);

    void addAll(List<Pair<String, T>> pairs);

    void removeAll(List<Pair<String, T>> pairs);

    T find(String key);

    List<T> findAll(List<String> keys);

}
