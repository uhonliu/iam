/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2016-2018
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
package it.infn.mw.iam.api.account.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import it.infn.mw.iam.api.common.OffsetPageable;
import it.infn.mw.iam.api.common.PagedResourceService;
import it.infn.mw.iam.persistence.model.IamAccount;
import it.infn.mw.iam.persistence.repository.IamAccountRepository;

@Service
public class DefaultPagedAccountsService implements PagedResourceService<IamAccount> {

  @Autowired
  private IamAccountRepository accountRepository;

  @Override
  public Page<IamAccount> getPage(OffsetPageable op) {

    return accountRepository.findAll(op);
  }

  @Override
  public long count() {

    return accountRepository.count();
  }

  @Override
  public Page<IamAccount> getPage(OffsetPageable op, String filter) {

    filter = String.format("%%%s%%", filter);
    return accountRepository.findByFilter(filter, op);
  }

  @Override
  public long count(String filter) {

    filter = String.format("%%%s%%", filter);
    return accountRepository.countByFilter(filter);
  }

}
