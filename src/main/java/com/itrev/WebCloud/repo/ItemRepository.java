package com.itrev.WebCloud.repo;

import com.itrev.WebCloud.models.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item,Long> {

}
