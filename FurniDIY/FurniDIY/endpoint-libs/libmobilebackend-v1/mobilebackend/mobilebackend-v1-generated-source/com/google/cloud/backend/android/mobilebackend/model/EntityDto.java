/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2013-09-16 16:01:30 UTC)
 * on 2013-10-16 at 18:29:04 UTC 
 * Modify at your own risk.
 */

package com.google.cloud.backend.android.mobilebackend.model;

/**
 * Model definition for EntityDto.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the mobilebackend. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class EntityDto extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private com.google.api.client.util.DateTime createdAt;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String createdBy;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String kindName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String owner;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Object properties;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private com.google.api.client.util.DateTime updatedAt;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String updatedBy;
  
  @com.google.api.client.util.Key
  private java.lang.String content;
  
  @com.google.api.client.util.Key
  private java.lang.String binary;

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getCreatedAt() {
    return createdAt;
  }

  /**
   * @param createdAt createdAt or {@code null} for none
   */
  public EntityDto setCreatedAt(com.google.api.client.util.DateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getCreatedBy() {
    return createdBy;
  }

  /**
   * @param createdBy createdBy or {@code null} for none
   */
  public EntityDto setCreatedBy(java.lang.String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public EntityDto setId(java.lang.String id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getKindName() {
    return kindName;
  }

  /**
   * @param kindName kindName or {@code null} for none
   */
  public EntityDto setKindName(java.lang.String kindName) {
    this.kindName = kindName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getOwner() {
    return owner;
  }

  /**
   * @param owner owner or {@code null} for none
   */
  public EntityDto setOwner(java.lang.String owner) {
    this.owner = owner;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Object getProperties() {
    return properties;
  }

  /**
   * @param properties properties or {@code null} for none
   */
  public EntityDto setProperties(java.lang.Object properties) {
    this.properties = properties;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getUpdatedAt() {
    return updatedAt;
  }

  /**
   * @param updatedAt updatedAt or {@code null} for none
   */
  public EntityDto setUpdatedAt(com.google.api.client.util.DateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getUpdatedBy() {
    return updatedBy;
  }
  
  public EntityDto setContnet(java.lang.String content) {
	    this.content = content;
	    return this;
	  }
  public java.lang.String getContent() {
		
		return content;
	}
  
  public EntityDto setBinary(java.lang.String binary) {
	    this.binary = binary;
	    return this;
	  }
public java.lang.String getBinary() {
		
		return binary;
	}


  /**
   * @param updatedBy updatedBy or {@code null} for none
   */
  public EntityDto setUpdatedBy(java.lang.String updatedBy) {
    this.updatedBy = updatedBy;
    return this;
  }

  @Override
  public EntityDto set(String fieldName, Object value) {
    return (EntityDto) super.set(fieldName, value);
  }

  @Override
  public EntityDto clone() {
    return (EntityDto) super.clone();
  }
}