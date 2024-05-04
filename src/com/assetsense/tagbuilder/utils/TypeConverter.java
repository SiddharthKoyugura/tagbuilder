package com.assetsense.tagbuilder.utils;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.c2.domain.Lookup;
import com.assetsense.tagbuilder.c2.domain.Observation;
import com.assetsense.tagbuilder.c2.domain.Tag;
import com.assetsense.tagbuilder.dto.AssetDTO;
import com.assetsense.tagbuilder.dto.ObservationDTO;
import com.assetsense.tagbuilder.dto.TagDTO;

public class TypeConverter {
	public AssetDTO convertToAssetDTO(Asset asset){
		AssetDTO assetDTO = new AssetDTO();
        assetDTO.setId(asset.getId());
        assetDTO.setName(asset.getName());
        assetDTO.setEcn(asset.getEcn());
        assetDTO.setLocation(asset.getLocation());
        assetDTO.setCategory(asset.getCategory());
        assetDTO.setIsCompleted(asset.getIsCompleted());

        Lookup model = asset.getModel();
        if (model != null) {
            assetDTO.setModel(model);
        }

        Lookup assettype = asset.getAssettype();
        if (assettype != null) {
            assetDTO.setAssettype(assettype);
        }

        Lookup supplierName = asset.getSupplierName();
        if (supplierName != null) {
            assetDTO.setSupplierName(supplierName);
        }

        for (Asset childAsset : asset.getChildAssets()) {
        	assetDTO.getChildAssets().add(convertToAssetDTO(childAsset));
        }

        for (Observation observation : asset.getObservations()) {
            assetDTO.getObservations().add(convertToObservationDTO(observation));
        }

        return assetDTO;
	}
	
	public ObservationDTO convertToObservationDTO(Observation observation){
		ObservationDTO obsDTO = new ObservationDTO();
		
		obsDTO.setId(observation.getId());
		obsDTO.setCode(observation.getCode());
		obsDTO.setInputType(observation.getInputType());
		obsDTO.setDescription(observation.getDescription());
		obsDTO.setCategory(observation.getCategory());
		obsDTO.setMeasurement(observation.getMeasurement());
		obsDTO.setUnitid(observation.getUnitid());
		
		return obsDTO;
	}
	
	public TagDTO convertToTagDTO(Tag tag) {
		TagDTO tagDTO = new TagDTO();
		
		tagDTO.setId(tag.getId());
		tagDTO.setName(tag.getName());
		tagDTO.setCategory(tag.getCategory());
		tagDTO.setAsset(convertToAssetDTO(tag.getAsset()));
		tagDTO.setObservation(convertToObservationDTO(tag.getObservation()));
		
		return tagDTO;
	}
	
	public Asset convertToAsset(AssetDTO assetDTO){
		Asset asset = new Asset();
        asset.setId(assetDTO.getId());
        asset.setName(assetDTO.getName());
        asset.setEcn(assetDTO.getEcn());
        asset.setLocation(assetDTO.getLocation());
        asset.setCategory(assetDTO.getCategory());
        asset.setIsCompleted(assetDTO.getIsCompleted());

        Lookup model = assetDTO.getModel();
        if (model != null) {
            asset.setModel(model);
        }

        Lookup assettype = assetDTO.getAssettype();
        if (assettype != null) {
            asset.setAssettype(assettype);
        }

        Lookup supplierName = assetDTO.getSupplierName();
        if (supplierName != null) {
            asset.setSupplierName(supplierName);
        }

        for (AssetDTO childAsset : assetDTO.getChildAssets()) {
        	asset.getChildAssets().add(convertToAsset(childAsset));
        }

        for (ObservationDTO observation : assetDTO.getObservations()) {
            asset.getObservations().add(convertToObservation(observation));
        }

        return asset;
	}
	
	public Observation convertToObservation(ObservationDTO observationDTO){
		Observation observation = new Observation();
		
		observation.setId(observationDTO.getId());
		observation.setCode(observationDTO.getCode());
		observation.setInputType(observationDTO.getInputType());
		observation.setDescription(observationDTO.getDescription());
		observation.setCategory(observationDTO.getCategory());
		observation.setMeasurement(observationDTO.getMeasurement());
		observation.setUnitid(observationDTO.getUnitid());
		
		return observation;
	}
}
