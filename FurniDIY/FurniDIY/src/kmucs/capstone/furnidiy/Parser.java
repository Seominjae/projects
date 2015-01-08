package kmucs.capstone.furnidiy;

import java.io.IOException;

// yhchung
// parsing

public class Parser {

	private Model model;
	private TypeReader reader;
	private Logger logger;
	private ModelObject currentObject;

	// yhchung 140524 추가
	private int numFaces;

	//

	public Parser(TypeReader reader) {
		this.reader = reader;
	}

	public Model parseFile() throws ParserException {
		try {
			int limit = readChunk();
			while (reader.position() < limit) {
				readChunk();
			}
		} catch (IOException e) {
			throw new ParserException(e);
		}
		return model;
	}

	private int readChunk() throws IOException {
		short type = reader.getShort();
		int size = reader.getInt(); // this is probably unsigned but for
									// convenience we use signed int
		log("Chunk 0x%04x (%d)", type, size);
		parseChunk(type, size);
		return size;
	}

	private void parseChunk(short type, int size) throws IOException {
		switch (type) {
		case 0x0002:
			parseVersionChunk();
			break;
		case 0x3d3d:
			break;
		// data type이 none인데도 바로 skip하면 안되네
		case 0x3d3e: // mesh_version
			parseMeshVersionChunk();
			break;
		case 0x4000:
			parseObjectChunk();
			break;
		case 0x4100:
			break;
		case 0x4110:
			parseVerticesList();
			break;
		case 0x4120:
			parseFacesDescription();
			break;
		case 0x4140:
			parseMappingCoordinates();
			break;
		case 0x4160:
			parseLocalCoordinateSystem();
			break;
		case 0x4d4d:
			parseMainChunk();
			break;
		case (short) 0xafff: // Material block
			break;
		case (short) 0xa000: // Material name
			parseMaterialName();
			break;
		case (short) 0xa200: // Texture map 1
			break;
		case (short) 0xa300: // Mapping filename
			parseMappingFilename();
			break;

		case 0x7890: // AngleData를 위한 임의의 코드. <-- 3ds 규격 아님
			parseAngleData();
			break;

		// yhchung 140524 추가 시작
		case 0x0100: // master_scale
			parseMasterScale();
			break;
		case 0x0011: // color_24
			parseColorRGB();
			break;
		case 0x0012: // lin_color_24
			parseLinColorRGB();
			break;
		case 0x0030: // int_percentage
			parseIntPercentage();
			break;
		case (short) 0xa100: // mat_shading
			parseMatShading();
			break;
		case (short) 0xa087: // mat_wiresize
			parseMatWireSize();
			break;
		case 0x4700: // n_camera
			parseNCamera();
			break;
		case 0x4720: // cam_ranges
			parseCamRanges();
			break;
		case 0x4130: // msh_mat_group
			parseMshMatGroup();
			break;
		case 0x4150: // smooth_group
			parseSmoothGroup();
			break;
		case (short) 0xb00a: // kfhdr
			parseKFHDR();
			break;
		case (short) 0xb008: // kfseg
			parseKFSEG();
			break;
		case (short) 0xb009: // kfcurtime
			parseKFCURTIME();
			break;
		case (short) 0xb030: // node_id
			parseNodeID();
			break;
		case (short) 0xb010: // node_hdr
			parseNodeHDF();
			break;
		case (short) 0xb020: // pos_track_tag
			parsePosTrackTag();
			break;
		case (short) 0xb023: // fov_track_tag
			parseFovTrackTag();
			break;
		case (short) 0xb024: // roll_track_tag
			parseRollTrackTag();
			break;
		case (short) 0xb013: // pivot
			parsePivot();
			break;
		case (short) 0xb021: // rot_track_tag
			parseRotTrackTag();
			break;
		case (short) 0xb022: // scl_track_tag
			parseSclTrackTag();
			break;
		case (short) 0xa351: // mat_map_tiling
			parseMatMapTiling();
			break;
		case (short) 0xa353: // mat_map_texblur
			parseMatMapTexBlut();
			break;
		case (short) 0xa354: // mat_map_uscale
			parseMatMapUScale();
			break;
		case (short) 0xa356: // mat_map_vscale
			parseMatMapVScale();
			break;
		case (short) 0xa358: // mat_map_uoffset
			parseMatMapUOffset();
			break;
		case (short) 0xa35a: // mat_map_voffset
			parseMatMapVOffset();
			break;
		case (short) 0xa35c: // mat_map_ang
			parseMatMapAng();
			break;
		case (short) 0xa360: // mat_map_col1
			parseMatMapCol1();
			break;
		case (short) 0xa362: // mat_map_col2
			parseMatMapCol2();
			break;
		case (short) 0xa364: // mat_map_rcol
			parseMatMapRCol();
			break;
		case (short) 0xa366: // mat_map_gcol
			parseMatMapGCol();
			break;
		case (short) 0xa368: // mat_map_bcol
			parseMatMapBCol();
			break;
		case 0x7001: // viewport_layout
			parseViewportLayout();
			break;
		case 0x7020: // viewport_size
			parseViewportSize();
			break;
		case 0x0010: // color_f
			parseColorF();
			break;
		case 0x0013: // lin_color_f
			parseLinColorF();
			break;
		case 0x1200: // solid_bgnd
			parseSolidBgnd();
			break;
		case (short) 0xb011: // instance_name
			parseDummyName();
			break;
		case (short) 0xb014: // bound box <-- unknown type
			parseBoundBox();
			break;
		case (short) 0xb015: // morph_smooth
			parseMorphSmooth();
			break;
		case (short) 0xb025: // col_track_tag
			parseColTrackTag();
			break;

		case 0x7011: // viewport_data1 <-- 정확한 구조를 모름
		case 0x7012: // viewport_data2 <-- 정확한 구조를 모름
			/*
			 * 우선 skip하는 걸로 parseViewportData(); break;
			 */

		case 0x0000: // null_chunk
			if (size > 0)
				skipChunk(size);
			break;
		// yhchung 140524 추가 끝

		case 0x0005: // m3d_kfversion
		case 0x0995: // chunk type
		case 0x0996: // chunk unique
		case 0x0997: // not chunk
		case 0x0998: // container
		case 0x0999: // is chunk
		case 0x0c3c: // c_sxp_selfi_maskdata
		case 0x1101: // use_bit_map
		case 0x1201: // use_solid_bgnd
		case 0x1301: // use_v_gradient
		case 0x1401: // hi_shadow_bias
		case 0x1430: // ###shadow_samples? shadow_range?
						// shadow_filter(float)?###
		case 0x2100: // ambient_light
		case 0x2201: // use_fog
		case 0x2210: // fog_bgnd
		case 0x2301: // use_distance_cue
		case 0x2303: // use_layer_fog
		case 0x2310: // dcue_bgnd
		case 0x2d2d: // smagic
		case 0x2d3d: // lmagic
		case 0x3000: // defuault_view
		case 0x3090: // view_window
		case 0x4010: // obj_hidden
		case 0x4011: // obj_vis_lofter
		case 0x4012: // obj_doesnt_cast
		case 0x4013: // obj_matte
		case 0x4014: // obj_fast
		case 0x4015: // obj_procedural
		case 0x4016: // obj_frozen
		case 0x4017: // obj_dont_rcvshadow
		case 0x4181: // proc_name
		case 0x4182: // proc_data
		case 0x4190: // msh_boxmap
		case 0x4400: // n_d_l_old
		case 0x4500: // n_cam_old
		case 0x4620: // dl_off
		case 0x4625: // dl_attenuate
		case 0x4627: // dl_rayshad
		case 0x4630: // dl_shadowed
		case 0x4640: // dl_local_shadow
		case 0x4641: // dl_local_shadow2
		case 0x4650: // dl_see_cone
		case 0x4651: // dl_spot_rectangular
		case 0x4652: // dl_spot_overshoot
		case 0x4653: // dl_spot_projector
		case 0x4654: // dl_exclude
		case 0x4655: // dl_range
		case 0x4657: // dl_spot_aspect
		case 0x4680: // n_ambient_light
		case 0x4710: // cam_see_cone
		case 0x4f00: // hierarchy
		case 0x4f10: // parent_object
		case 0x4f20: // pivot_object
		case 0x4f30: // pviot_limits
		case 0x4f40: // pivot_order
		case 0x4f50: // xlate_range
		case 0x5000: // poly_2d
		case 0x5010: // shape_ok
		case 0x5011: // shape_not_ok
		case 0x5020: // shape_hook
		case 0x6000: // path_3d
		case 0x6005: // path_matrix
		case 0x6010: // shape_2d
		case 0x6020: // m_scale
		case 0x6030: // m_twist
		case 0x6040: // m_teeter
		case 0x6050: // m_fit
		case 0x6060: // m_bevel
		case 0x6070: // xz_curve
		case 0x6080: // yz_curve
		case 0x6090: // interpct
		case 0x60a0: // defrom_limit
		case 0x6100: // use_contour
		case 0x6110: // use_tween
		case 0x6120: // use_scale
		case 0x6130: // use_twist
		case 0x6140: // use_teeter
		case 0x6150: // use_fit
		case 0x6160: // use_bevel
		case 0x7010: // viewport_data_old
		case 0x7030: // network_view
		case (short) 0x8000: // xdata_section
		case (short) 0x8001: // xdata_entry
		case (short) 0x8002: // xdata_appname
		case (short) 0x8003: // xdata_string
		case (short) 0x8004: // xdata_float
		case (short) 0x8005: // xdata_double
		case (short) 0x8006: // xdata_short
		case (short) 0x8007: // xdata_long
		case (short) 0x8008: // xdata_void
		case (short) 0x8009: // xdata_group
		case (short) 0x800a: // xdata_rfu6
		case (short) 0x800b: // xdata_xfu5
		case (short) 0x800c: // xdata_rfu4
		case (short) 0x800d: // xdata_rfu3
		case (short) 0x800e: // xdata_rfu2
		case (short) 0x800f: // xdata_rfu1
		case (short) 0x80f0: // parent_name
		case (short) 0xa010: // mat_ambient
		case (short) 0xa020: // mat_diffuse
		case (short) 0xa030: // mat_specular
		case (short) 0xa040: // mat_shininess
		case (short) 0xa041: // mat_shin2pct
		case (short) 0xa042: // mat_shin3pct
		case (short) 0xa050: // mat_transparency
		case (short) 0xa052: // mat_xpfall
		case (short) 0xa053: // mat_refblur
		case (short) 0xa080: // mat_self_illum
		case (short) 0xa081: // mat_two_side
		case (short) 0xa082: // mat_decal
		case (short) 0xa083: // mat_additive
		case (short) 0xa084: // mat_self_ilpct
		case (short) 0xa085: // mat_wire
		case (short) 0xa086: // mat_supersmp
		case (short) 0xa088: // mat_facemap
		case (short) 0xa08a: // mat_xpfallin
		case (short) 0xa08c: // mat_phongsoft
		case (short) 0xa08e: // mat_wireabs
		case (short) 0xa204: // mat_specmap
		case (short) 0xa210: // mat_opacmap
		case (short) 0xa220: // mat_reflmap
		case (short) 0xa230: // mat_bumpmap
		case (short) 0xa240: // mat_use_xpfall
		case (short) 0xa250: // mat_use_refblur
		case (short) 0xa252: // mat_bump_percent
		case (short) 0xa310: // mat_acubic
		case (short) 0xa320: // mat_sxp_text_data
		case (short) 0xa321: // mat_sxp_text2_data
		case (short) 0xa322: // mat_sxp_opac_data
		case (short) 0xa324: // mat_sxp_bump_data
		case (short) 0xa325: // mat_sxp_spec_data
		case (short) 0xa326: // mat_sxp_shin_data
		case (short) 0xa328: // ma_sxp_slef1_data
		case (short) 0xa32a: // mat_sxp_text_data
		case (short) 0xa32c: // mat_sxp_text2_maskdata
		case (short) 0xa32e: // mat_sxp_opac_maskdat
		case (short) 0xa330: // mat_sxp_bump_maskdata
		case (short) 0xa332: // mat_sxp_spec_maskdata
		case (short) 0xa334: // ma_sxp_shin_maskdata
		case (short) 0xa336: // mas_sxp_self1_maskdata
		case (short) 0xa338: // mat_sxp_refl_maskdata
		case (short) 0xa33a: // mat_tex2map
		case (short) 0xa33c: // mat_shinmap
		case (short) 0xa33d: // mat_selfimap
		case (short) 0xa33e: // mat_texmask
		case (short) 0xa340: // mat_tex2mask
		case (short) 0xa342: // mat_opacmask
		case (short) 0xa344: // mat_bumpmask
		case (short) 0xa346: // mat_shinmask
		case (short) 0xa348: // mat_specmask
		case (short) 0xa34a: // mat_slefimask
		case (short) 0xa34c: // mat_reflmask
		case (short) 0xa350: // mat_map_tilingold
		case (short) 0xa352: // mat_map_texblur_old
		case (short) 0xb000: // KFDATA
		case (short) 0xb001: // ambient_node_tag
		case (short) 0xb002: // object_node_tag
		case (short) 0xb003: // camera_node_tag
		case (short) 0xb004: // target_node_tag
		case (short) 0xb005: // light_node_tag
		case (short) 0xb006: // l_target_node_tag
		case (short) 0xb007: // spotlight-node_tag
		case (short) 0xb012: // prescale
		case (short) 0xb029: // hide_track_tag
		case (short) 0xc010: // c_mdrawer
		case (short) 0xc020: // c_tdrawer
		case (short) 0xc030: // ch_shpdrawer
		case (short) 0xc040: // c_moddrawer
		case (short) 0xc050: // c_ripdrawer
		case (short) 0xc060: // c_txddrawer
		case (short) 0xc062: // c_pdrawer
		case (short) 0xc064: // c_mtldrawer
		case (short) 0xc066: // cflidrawer
		case (short) 0xc067: // c_cubdrawer
		case (short) 0xc070: // c_mfile
		case (short) 0xc080: // c_shpfile
		case (short) 0xc090: // c_modfile
		case (short) 0xc0a0: // c_ripfile
		case (short) 0xc0b0: // c_txfile
		case (short) 0xc0b2: // c_pfile
		case (short) 0xc0b4: // c_mtlfile
		case (short) 0xc0b6: // c_flifile
		case (short) 0xc0b8: // c_palfile
		case (short) 0xc0c0: // c_tx_string
		case (short) 0xc0d0: // c_consts
		case (short) 0xc0e0: // c_snaps
		case (short) 0xc0f0: // c_grids
		case (short) 0xc100: // c_asnaps
		case (short) 0xc110: // c_grid_range
		case (short) 0xc120: // c_rendtype
		case (short) 0xc130: // c_progmode
		case (short) 0xc140: // c_prevmode
		case (short) 0xc150: // c_modwmode
		case (short) 0xc160: // c_modmodel
		case (short) 0xc170: // c_all_lines
		case (short) 0xc180: // c_back_type
		case (short) 0xc190: // c_md_cs
		case (short) 0xc1a0: // c_md_ce
		case (short) 0xc1b0: // c_md_sml
		case (short) 0xc1c0: // c_md_smw
		case (short) 0xc1c3: // c_loft_with_texture
		case (short) 0xc1c4: // c_loft_l_repeat
		case (short) 0xc1c5: // c_loft_w_repreat
		case (short) 0xc1c6: // c_loft_uv_normalize
		case (short) 0xc1c7: // c_weld_loft
		case (short) 0xc1d0: // c_md_pdet
		case (short) 0xc1e0: // c_md_sdet
		case (short) 0xc1f0: // c_rgb_mode
		case (short) 0xc200: // c_rgb_hide
		case (short) 0xc202: // c_rgb_mapsw
		case (short) 0xc204: // c_rgb_twoside
		case (short) 0xc208: // c_rgb_shadow
		case (short) 0xc210: // c_rgb_aa
		case (short) 0xc220: // c_rgb_ovw
		case (short) 0xc230: // c_rgb_ovh
		case (short) 0xc23d: // cmagic
		case (short) 0xc240: // c_rgb_pictype
		case (short) 0xc250: // _rgb_output
		case (short) 0xc253: // c_rgb_todisk
		case (short) 0xc254: // c_rgb_compress
		case (short) 0xc255: // c_jpeg_compression
		case (short) 0xc256: // c_rgb_dispdev
		case (short) 0xc259: // c_rgb_harddev
		case (short) 0xc25a: // c_rgb_path
		case (short) 0xc25b: // c_bittmap_drawer
		case (short) 0xc260: // c_rgb_file
		case (short) 0xc270: // c_gb_ovaspect
		case (short) 0xc271: // c_rb_animtype
		case (short) 0xc272: // c_render_all
		case (short) 0xc273: // c_rend_from
		case (short) 0xc274: // c_rend_to
		case (short) 0xc275: // c_rend_nth
		case (short) 0xc276: // c_pal_type
		case (short) 0xc277: // c_rnd_turbo
		case (short) 0xc278: // c_rnd_mip
		case (short) 0xc279: // c_bgnd_method
		case (short) 0xc27a: // c_auto_reflect
		case (short) 0xc27b: // c_vp_from
		case (short) 0xc27c: // c_vp_to
		case (short) 0xc27d: // c_vp_nth
		case (short) 0xc27e: // c_rend_tstep
		case (short) 0xc27f: // c_vp_tstep
		case (short) 0xc280: // c_srdiam
		case (short) 0xc290: // c_srdeg
		case (short) 0xc2a0: // c_srseg
		case (short) 0xc2b0: // c_srdir
		case (short) 0xc2c0: // c_hetop
		case (short) 0xc2d0: // c_hebot
		case (short) 0xc2e0: // c-heht
		case (short) 0xc2f0: // c_heturns
		case (short) 0xc300: // c_hedeg
		case (short) 0xc310: // c_heseg
		case (short) 0xc320: // c_hedir
		case (short) 0xc330: // c_quikstuff
		case (short) 0xc340: // c_see_lights
		case (short) 0xc350: // c_see_cameras
		case (short) 0xc360: // c_see_3d
		case (short) 0xc370: // c_meshsel
		case (short) 0xc380: // c_meshunsel
		case (short) 0xc390: // c_polysel
		case (short) 0xc3a0: // c_polyunsel
		case (short) 0xc3a2: // c_shplocal
		case (short) 0xc3a4: // c_mshlocal
		case (short) 0xc3b0: // c_num_format
		case (short) 0xc3c0: // c_arch_denom
		case (short) 0xc3d0: // c_in_device
		case (short) 0xc3e0: // c_mscale
		case (short) 0xc3f0: // c_comm_port
		case (short) 0xc400: // c_tab_bases
		case (short) 0xc410: // c_tab_divs
		case (short) 0xc420: // c_master_scales
		case (short) 0xc430: // c_show_1stvert
		case (short) 0xc440: // c_shaper_ok
		case (short) 0xc450: // c_lofter_ok
		case (short) 0xc460: // c_editor_ok
		case (short) 0xc470: // c_keyframer_ok
		case (short) 0xc480: // c_picksize
		case (short) 0xc490: // c_maptype
		case (short) 0xc4a0: // c_map_display
		case (short) 0xc4b0: // c_tile_xy
		case (short) 0xc4c0: // c_map_xyz
		case (short) 0xc4d0: // c_map_scale
		case (short) 0xc4e0: // c_map_matrix_old
		case (short) 0xc4e1: // c_map_matrix
		case (short) 0xc4f0: // c_map_wid_ht
		case (short) 0xc500: // c_obname
		case (short) 0xc510: // c_camname
		case (short) 0xc520: // c_ltname
		case (short) 0xc525: // c_cur_mname
		case (short) 0xc526: // c_curmtl_from_mesh
		case (short) 0xc527: // c_get_shape_make_faces
		case (short) 0xc530: // c_detail
		case (short) 0xc540: // c_vertmark
		case (short) 0xc550: // c_mshax
		case (short) 0xc560: // c_mshcp
		case (short) 0xc570: // c_userax
		case (short) 0xc580: // c_shook
		case (short) 0xc590: // c_rax
		case (short) 0xc5a0: // c_stape
		case (short) 0xc5b0: // c_ltape
		case (short) 0xc5c0: // c_etape
		case (short) 0xc5c8: // c_ktape
		case (short) 0xc5d0: // c_sphsegs
		case (short) 0xc5e0: // c_geosmooth
		case (short) 0xc5f0: // c_hemisegs
		case (short) 0xc600: // c_prismsegs
		case (short) 0xc610: // c_prismides
		case (short) 0xc620: // c_tubesegs
		case (short) 0xc630: // c_tubesides
		case (short) 0xc640: // c_torsegs
		case (short) 0xc650: // c_torsides
		case (short) 0xc660: // c_coneside
		case (short) 0xc661: // c_conessegs
		case (short) 0xc670: // c_ngparms
		case (short) 0xc680: // c_pthlelvel
		case (short) 0xc690: // c_mscsym
		case (short) 0xc6a0: // c_mftsym
		case (short) 0xc6b0: // c_mttsym
		case (short) 0xc6c0: // c_smoothing
		case (short) 0xc6d0: // c_modicount
		case (short) 0xc6e0: // c_fontsel
		case (short) 0xc6f0: // c_tess_type
		case (short) 0xc6f1: // c_tess_tension
		case (short) 0xc700: // c_seg_start
		case (short) 0xc705: // c_seg_end
		case (short) 0xc710: // c_curtime
		case (short) 0xc715: // c_animlength
		case (short) 0xc720: // c_pv_from
		case (short) 0xc725: // c_pv_to
		case (short) 0xc730: // c_pv_dofnum
		case (short) 0xc735: // c_pv_rng
		case (short) 0xc740: // c_pv_nth
		case (short) 0xc745: // c_pv_tye
		case (short) 0xc750: // c_pv_method
		case (short) 0xc755: // c_pv_fps
		case (short) 0xc765: // c_vtf_frames
		case (short) 0xc770: // c_vtr_hdtl
		case (short) 0xc771: // c_vtr_hd
		case (short) 0xc772: // c_vtr_tl
		case (short) 0xc775: // c_vtr_in
		case (short) 0xc780: // c_vtr_pk
		case (short) 0xc785: // c_vtr_sh
		case (short) 0xc790: // c_work_mtls
		case (short) 0xc792: // c_work_mtls_2
		case (short) 0xc793: // c_work_mtls_3
		case (short) 0xc794: // c_work_mtls_4
		case (short) 0xc7a1: // c_gbtype
		case (short) 0xc7b0: // c_medtile
		case (short) 0xc7d0: // c_lo_contrast
		case (short) 0xc7d1: // c_hi_contrast
		case (short) 0xc7e0: // c_froz_display
		case (short) 0xc7f0: // c-boolweld
		case (short) 0xc7f1: // c-booltype
		case (short) 0xc900: // c_and_thresh
		case (short) 0xc901: // c_ss_thresh
		case (short) 0xc903: // c_texture_blue_default
		case (short) 0xca00: // c_mapdrawer
		case (short) 0xca01: // c_mapdrawer1
		case (short) 0xca02: // c_mapdrawer2
		case (short) 0xca03: // c_mapdrawer3
		case (short) 0xca04: // c_mapdrawer4
		case (short) 0xca05: // c_mapdrawer5
		case (short) 0xca06: // c_mapdrawer6
		case (short) 0xca07: // c_mapdrawer7
		case (short) 0xca08: // c_mapdrawer8
		case (short) 0xca09: // c_mapdrawer9
		case (short) 0xca10: // c_mapdrawer_entry
		case (short) 0xca20: // c_backup_file
		case (short) 0xca21: // c_dither_256
		case (short) 0xca22: // c_save_list
		case (short) 0xca23: // c_use_alpha
		case (short) 0xca24: // c_tga_depth
		case (short) 0xca25: // c_rend_fields
		case (short) 0xca26: // c_reflip
		case (short) 0xca27: // c_sel_itemtog
		case (short) 0xca28: // c_sel_reset
		case (short) 0xca29: // c_sticky_keyinf
		case (short) 0xca2a: // c_weld_threshold
		case (short) 0xca2b: // c_zclip_point
		case (short) 0xca2c: // c-alpha_split
		case (short) 0xca30: // c_kf_show_backface
		case (short) 0xca40: // c_optimize_loft
		case (short) 0xca42: // c_tens_default
		case (short) 0xca44: // c_cont_default
		case (short) 0xca46: // c_bias_defulat
		case (short) 0xca50: // c_dxfname_src
		case (short) 0xca60: // c_auto_weld
		case (short) 0xca70: // c_auto_unify
		case (short) 0xca80: // c_auto_smooth
		case (short) 0xca90: // c_dxf_smooth_ang
		case (short) 0xcaa0: // c_smooth_ang
		case (short) 0xcb00: // c_work_mtls_5
		case (short) 0xcb01: // c_work_mtls_6
		case (short) 0xcb02: // c_work_mtls_7
		case (short) 0xcb03: // c_work_mtls_8
		case (short) 0xcb04: // c_workmtl
		case (short) 0xcb10: // c_sxp_text_data
		case (short) 0xcb11: // c_sxp_opac_data
		case (short) 0xcb12: // c_sxp_bump_data
		case (short) 0xcb13: // c_sxp_shin_data
		case (short) 0xcb20: // c_sxp_text2_data
		case (short) 0xcb24: // c_sxp_spec_data
		case (short) 0xcb28: // c_sxp_self_data
		case (short) 0xcb30: // c_sxp_text_maskdata
		case (short) 0xcb32: // c_sxp_text2_maskdata
		case (short) 0xcb34: // c_sxp_opac_maskdata
		case (short) 0xcb36: // c_sxp_bump_maskdata
		case (short) 0xcb38: // c_sxp_spec_maskdata
		case (short) 0xcb3a: // c_sxp_shin_maskdata
		case (short) 0xcb3e: // c_sxp_refl_maskdata
		case (short) 0xcc00: // c_net_use_vpost
		case (short) 0xcc10: // c_net_use_gamma
		case (short) 0xcc20: // c_net_field_order
		case (short) 0xcd00: // c_blur_frames
		case (short) 0xcd10: // c_blur_samples
		case (short) 0xcd20: // c_blur_dur
		case (short) 0xcd30: // c_hot_method
		case (short) 0xcd40: // c_hot_check
		case (short) 0xcd50: // c_pixel_size
		case (short) 0xcd60: // c_disp_gamma
		case (short) 0xcd70: // c_fbuf_gamma
		case (short) 0xcd80: // c_file_out_gamma
		case (short) 0xcd82: // c_file_in_gamma
		case (short) 0xcd84: // c_gamma_correct
		case (short) 0xcd90: // c_apply_disp_gamma
		case (short) 0xcda0: // c_apply_fbuf_gamma
		case (short) 0xcdb0: // c_apply_file gamma
		case (short) 0xcdc0: // c-force_wire
		case (short) 0xcdd0: // c_ray_shadows
		case (short) 0xcde0: // c_master_ambient
		case (short) 0xcdf0: // c_super_sample
		case (short) 0xce00: // c_object_mbur
		case (short) 0xce10: // c_mblur_dither
		case (short) 0xce20: // c_dither_24
		case (short) 0xce30: // c_super_black
		case (short) 0xce40: // c_safe_frame
		case (short) 0xce50: // c_view_pres_ratio
		case (short) 0xce60: // c_bgnd_pres_ratio
		case (short) 0xce70: // c_nth_serial_num
		case (short) 0xd000: // vpdata
		case (short) 0xd100: // p_queue_entry
		case (short) 0xd110: // p_queue_image
		case (short) 0xd114: // p_queue_useigamma
		case (short) 0xd120: // p_queue_proc
		case (short) 0xd130: // p_queue_solid
		case (short) 0xd140: // p_queue_gradient
		case (short) 0xd150: // p_queue_kf
		case (short) 0xd152: // p_queue_motblur
		case (short) 0xd153: // p_queue_mb_repeat
		case (short) 0xd160: // p_queue_none
		case (short) 0xd180: // p_queue_resize
		case (short) 0xd185: // p_queue_offset
		case (short) 0xd190: // p_queue_align
		case (short) 0xd1a0: // p_custom_size
		case (short) 0xd210: // p_alph_none
		case (short) 0xd220: // p_alph_pseudo
		case (short) 0xd221: // p_alph_op_pseudo
		case (short) 0xd222: // p_alph_blur
		case (short) 0xd225: // p_alph_pcol
		case (short) 0xd230: // p_alph_co
		case (short) 0xd231: // p_alph_op_key
		case (short) 0xd235: // p_alph_kcol
		case (short) 0xd238: // p_alph_op_noconv
		case (short) 0xd240: // p_alph_image
		case (short) 0xd250: // p_alph_alpha
		case (short) 0xd260: // p_alph_ques
		case (short) 0xd265: // p_alph_queimg
		case (short) 0xd270: // p_alph_cutoff
		case (short) 0xd280: // p_alphaneg
		case (short) 0xd300: // p_tran_none
		case (short) 0xd310: // p_tran_image
		case (short) 0xd312: // p_tran_frames
		case (short) 0xd320: // p_tran_fadein
		case (short) 0xd330: // p_tran_fadeout
		case (short) 0xd340: // p_tranneg
		case (short) 0xd400: // p_ranges
		case (short) 0xd500: // p_proc_data
		case (short) 0xf020: // pos_track_tag_key
		case (short) 0xf021: // rot_track_tag_kay
		case (short) 0xf022: // scl_track_tag_kay
		case (short) 0xf023: // fov_track_tag_key
		case (short) 0xf024: // roll_track_tag_key
		case (short) 0xf025: // col_track_tag_key
		case (short) 0xf026: // morph_track_tag_key
		case (short) 0xf027: // hot_track_tag_key
		case (short) 0xf028: // fall_track_tag_key
		case (short) 0xf110: // point_array_entry
		case (short) 0xf120: // point_flag_array_entry
		case (short) 0xf130: // face_array_entry
		case (short) 0xf140: // tex_verts_entry
		case (short) 0xf150: // smooth_group_entry
		case (short) 0xffff: // dummy
			break;

		case 0x0001: // unknown
		case 0x0031: // float_percentage
		case 0x1100: // bit_map
		case 0x1300: // v_gradient
		case 0x1400: // lo_shadow_bias
		case 0x1420: // shadow_map_size
		case 0x1460: // ray_bias
		case 0x1500: // o_consts
		case 0x2200: // fog
		case 0x2300: // distance_cue
		case 0x2302: // layer_fog
		case 0x3010: // view_top
		case 0x3020: // view_bottom
		case 0x3030: // view_left
		case 0x3040: // view_right
		case 0x3050: // view_front
		case 0x3060: // view_back
		case 0x3070: // view_user
		case 0x3080: // view_camera
		case 0x3DAA: // mlibmagic
		case 0x3DC2: // prjmagic
		case 0x3DFF: // matmagic
		case 0x4111: // point_flag_array
		case 0x4170: // mesh_texture_info
		case 0x4600: // n_direct_light
		case 0x4610: // dl_spotlight
		case 0x4656: // dl_spot_roll
		case 0x4658: // dl_ray_bias
		case 0x4659: // dl_inner_range
		case 0x465A: // dl_outer_range
		case 0x465B: // dl_multiplier
		case (short) 0xb026: // morph-track_tag
		case (short) 0xb027: // hot_track_tag
		case (short) 0xb028: // fall_track_tag
			parseOthers(type);
			break;

		default:
			log("Skip Chunk");
			skipChunk(size);
		}
	}

	private void skipChunk(int size) throws IOException {
		move(size - 6); // size includes headers. header is 6 bytes
	}

	private void move(int i) throws IOException {
		reader.skip(i);
	}

	private void parseOthers(short type) {
		log("*** parsing others...0x%04x", type);
		log("*** need more informations...");
	}

	// yhchung 140524 추가 시작

	private void parseColTrackTag() throws IOException {
		int flags = reader.getShort();
		int[] unknown = new int[4];
		for (int i = 0; i < unknown.length; i++) {
			unknown[i] = reader.getShort();
		}
		int keys = reader.getShort();
		int unknown2 = reader.getShort();

		for (int i = 0; i < keys; i++) {
			int frameNum = reader.getShort();
			int unknown3 = reader.getInt();
			float[] color = new float[3];
			for (int j = 0; j < color.length; j++) {
				color[i] = reader.getFloat();
			}
		}

		log("COL TRACK TAG : %d %d", flags, keys);
	}

	private void parseMorphSmooth() throws IOException {
		float data = reader.getFloat();

		log("Dummy Name: %f", data);
	}

	private void parseBoundBox() throws IOException {
		// unknown type. data size 보고 대충 때려 맞춤
		float[] data = new float[6];
		for (int i = 0; i < data.length; i++) {
			data[i] = reader.getFloat();
		}

		log("BoundBox: %f", data[0]);
	}

	private void parseDummyName() throws IOException {
		String name = reader.readString();

		log("Dummy Name: %s", name);
	}

	private void parseSolidBgnd() throws IOException {
		float[] data = new float[3];
		for (int i = 0; i < data.length; i++) {
			data[i] = reader.getFloat();
		}

		log("SOLIE BGND : %f %f %f", data[0], data[1], data[2]);
	}

	private void parseLinColorF() throws IOException {
		float[] data = new float[3];
		for (int i = 0; i < data.length; i++) {
			data[i] = reader.getFloat();
		}

		log("LIN COLOR F : %f %f %f", data[0], data[1], data[2]);
	}

	private void parseColorF() throws IOException {
		float[] data = new float[3];
		for (int i = 0; i < data.length; i++) {
			data[i] = reader.getFloat();
		}

		log("COLOR F : %f %f %f", data[0], data[1], data[2]);
	}

	private void parseViewportData() throws IOException {
		// 일부 문서에는 아래와 같은 데, 실제로 loading하면 안맞음
		int[] data = new int[7];
		for (int i = 0; i < data.length; i++) {
			data[i] = reader.getShort();
		}
		float[] fData = new float[6];
		for (int i = 0; i < data.length; i++) {
			fData[i] = reader.getFloat();
		}
		String sData = reader.readString();

		log("Viewport Data : %s", sData);
	}

	private void parseViewportSize() throws IOException {
		int[] data = new int[4];
		for (int i = 0; i < data.length; i++) {
			data[i] = reader.getShort();
		}

		log("Viewport Size");
	}

	private void parseViewportLayout() throws IOException {
		int[] data = new int[7];
		for (int i = 0; i < data.length; i++) {
			data[i] = reader.getShort();
		}

		log("Viewport Layout");
	}

	private void parseMatMapBCol() throws IOException {
		// default 00 00 FF
		byte byteR = reader.getByteData();
		byte byteG = reader.getByteData();
		byte byteB = reader.getByteData();

		log("Mat Map BCol : %d %d %d", byteR, byteG, byteB);
	}

	private void parseMatMapGCol() throws IOException {
		// default 00 FF 00
		byte byteR = reader.getByteData();
		byte byteG = reader.getByteData();
		byte byteB = reader.getByteData();

		log("Mat Map GCol : %d %d %d", byteR, byteG, byteB);
	}

	private void parseMatMapRCol() throws IOException {
		// default FF 00 00
		byte byteR = reader.getByteData();
		byte byteG = reader.getByteData();
		byte byteB = reader.getByteData();

		log("Mat Map RCol : %d %d %d", byteR, byteG, byteB);
	}

	private void parseMatMapCol2() throws IOException {
		// default FF FF FF
		byte byteR = reader.getByteData();
		byte byteG = reader.getByteData();
		byte byteB = reader.getByteData();

		log("Mat Map Col2 : %d %d %d", byteR, byteG, byteB);
	}

	private void parseMatMapCol1() throws IOException {
		// default 00 00 00
		byte byteR = reader.getByteData();
		byte byteG = reader.getByteData();
		byte byteB = reader.getByteData();

		log("Mat Map Col1 : %d %d %d", byteR, byteG, byteB);

	}

	private void parseMatMapAng() throws IOException {
		float val = reader.getFloat();
		log("Mat Map Angle : %f", val);
	}

	private void parseMatMapVOffset() throws IOException {
		float val = reader.getFloat();
		log("Mat Map VOffset : %f", val);
	}

	private void parseMatMapUOffset() throws IOException {
		float val = reader.getFloat();
		log("Mat Map UOffset : %f", val);
	}

	private void parseMatMapVScale() throws IOException {
		float val = reader.getFloat();
		log("Mat Map VScale : %f", val);
	}

	private void parseMatMapUScale() throws IOException {
		float val = reader.getFloat();
		log("Mat Map UScale : %f", val);
	}

	private void parseMatMapTexBlut() throws IOException {
		float val = reader.getFloat();
		log("Mat Map TexBlur : %f", val);
	}

	private void parseMatMapTiling() throws IOException {
		int flags = reader.getShort();
		log("Mat Map Tiling : %d", flags);
	}

	private void parseSclTrackTag() throws IOException {
		int flags = reader.getShort();
		log("SCL TRACK TAG : %d", flags);

		int[] unknown = new int[4];
		for (int i = 0; i < unknown.length; i++) {
			unknown[i] = reader.getShort();
			log("unknown[%d/4]: %d", i + 1, unknown[i]);
		}
		int keys = reader.getShort();
		int unknown2 = reader.getShort();
		log("key(%d), unknown(%d)", keys, unknown2);

		for (int i = 0; i < keys; i++) {
			int frameNum = reader.getShort();
			int unknown3 = reader.getInt();
			float[] scale = new float[3];
			for (int j = 0; j < scale.length; j++) {
				scale[j] = reader.getFloat();
			}
			log("SCL%d: frame%d, unknown%d, scale(%f, %f, %f)", i, frameNum,
					unknown3, scale[0], scale[1], scale[2]);
		}
	}

	private void parseRotTrackTag() throws IOException {
		int flags = reader.getShort();
		log("ROT TRACK TAG : %d", flags);

		int[] unknown = new int[4];
		for (int i = 0; i < unknown.length; i++) {
			unknown[i] = reader.getShort();
			log("unknown[%d/4]: %d", i + 1, unknown[i]);
		}
		int keys = reader.getShort();
		int unknown2 = reader.getShort();
		log("key(%d), unknown(%d)", keys, unknown2);

		for (int i = 0; i < keys; i++) {
			int frameNum = reader.getShort();
			int unknown3 = reader.getInt();
			float rot = reader.getFloat();
			float[] axis = new float[3];
			for (int j = 0; j < axis.length; j++) {
				axis[j] = reader.getFloat();
			}
			log("ROT%d: frame%d, unknown%d, rot%f, rot(%f, %f, %f)", i,
					frameNum, unknown3, rot, axis[0], axis[1], axis[2]);
		}
	}

	private void parsePivot() throws IOException {
		float[] pivot = new float[3];
		for (int i = 0; i < pivot.length; i++) {
			pivot[i] = reader.getFloat();
		}

		log("PIVOT : %f %f %f", pivot[0], pivot[1], pivot[2]);
	}

	private void parseRollTrackTag() throws IOException {
		int flags = reader.getShort();
		log("ROLL TRACK TAG : %d", flags);

		int[] unknown = new int[4];
		for (int i = 0; i < unknown.length; i++) {
			unknown[i] = reader.getShort();
			log("unknown[%d/4]: %d", i + 1, unknown[i]);
		}
		int keys = reader.getShort();
		int unknown2 = reader.getShort();
		log("key(%d), unknown(%d)", keys, unknown2);

		for (int i = 0; i < keys; i++) {
			int frameNum = reader.getShort();
			int unknown3 = reader.getInt();
			float camRoll = reader.getFloat();
			log("ROLL%d: frame%d, unknown%d, camRoll%f", i, frameNum, unknown3,
					camRoll);
		}
	}

	private void parseFovTrackTag() throws IOException {
		int flags = reader.getShort();
		log("FOV TRACK TAG : %d", flags);

		int[] unknown = new int[4];
		for (int i = 0; i < unknown.length; i++) {
			unknown[i] = reader.getShort();
			log("unknown[%d/4]: %d", i + 1, unknown[i]);
		}
		int keys = reader.getShort();
		int unknown2 = reader.getShort();
		log("key(%d), unknown(%d)", keys, unknown2);

		for (int i = 0; i < keys; i++) {
			int frameNum = reader.getShort();
			int unknown3 = reader.getInt();
			float fov = reader.getFloat();
			log("FOV%d: frame%d, unknown%d, fov%f", i, frameNum, unknown3, fov);
		}
	}

	private void parsePosTrackTag() throws IOException {
		int flags = reader.getShort();
		log("POS TRACK TAG : %d", flags);

		int[] unknown = new int[4];
		for (int i = 0; i < unknown.length; i++) {
			unknown[i] = reader.getShort();
			log("unknown[%d/4]: %d", i + 1, unknown[i]);
		}
		int keys = reader.getShort();
		int unknown2 = reader.getShort();
		log("key(%d), unknown(%d)", keys, unknown2);

		for (int i = 0; i < keys; i++) {
			int frameNum = reader.getShort();
			int unknown3 = reader.getInt();
			float[] pos = new float[3];
			for (int j = 0; j < pos.length; j++) {
				pos[j] = reader.getFloat();
			}
			log("POS%d: frame%d, unknown%d, pos(%f,%f,%f)", i, frameNum,
					unknown3, pos[0], pos[1], pos[2]);
		}
	}

	private void parseNodeHDF() throws IOException {
		String name = reader.readString();
		int[] info = new int[3];
		for (int i = 0; i < info.length; i++) {
			info[0] = reader.getShort();
		}
		log("Node Header : %s %d %d %d", name, info[0], info[1], info[2]);
	}

	private void parseNodeID() throws IOException {
		int id = reader.getShort();
		log("NodeID : %d", id);
	}

	private void parseKFCURTIME() throws IOException {
		int curFrame = reader.getShort();
		int other = reader.getShort();
		log("KFCURTIME : %d", curFrame);
	}

	private void parseKFSEG() throws IOException {
		int[] seg = new int[2];
		for (int i = 0; i < seg.length; i++) {
			seg[i] = reader.getShort();
		}
		int other = reader.getInt();
		log("KFSEG : %d, %d, %d", seg[0], seg[1], other);
	}

	private void parseKFHDR() throws IOException {
		int revNumber = reader.getShort();
		String name = reader.readString();
		int aniLength = reader.getInt();
		// 문서에는 short인데 int
		// int aniLength = reader.getShort();
		log("KFHDR : %d, %s, %d", revNumber, name, aniLength);
	}

	private void parseSmoothGroup() throws IOException {
		log("Smooth Group : %d", numFaces);
		int[] group = new int[numFaces * 2];
		for (int i = 0; i < group.length; i++) {
			group[i] = reader.getShort();
			log("Group[%d] : %d", i, group[i]);
		}
	}

	private void parseMshMatGroup() throws IOException {
		String name = reader.readString();
		int numFace = reader.getShort();
		log("Msh Mat Group : %s(%d)", name, numFace);

		int[] faces = new int[numFace];
		for (int i = 0; i < faces.length; i++) {
			faces[i] = reader.getShort();
			log("Face[%d] : %d", i, faces[i]);
		}
	}

	private void parseCamRanges() throws IOException {
		float[] range = new float[2];
		for (int i = 0; i < range.length; i++) {
			range[i] = reader.getFloat();
		}
		log("Cam Range : %f, %f", range[0], range[1]);
	}

	private void parseNCamera() throws IOException {
		float[] camera = new float[3];
		for (int i = 0; i < camera.length; i++) {
			camera[i] = reader.getFloat();
		}

		float[] target = new float[3];
		for (int i = 0; i < target.length; i++) {
			target[i] = reader.getFloat();
		}

		float angle = reader.getFloat();
		float focus = reader.getFloat();

		log("Camera : (%f,%f,%f) (%f,%f,%f) (%f,%f)", camera[0], camera[1],
				camera[2], target[0], target[1], target[2], angle, focus);
	}

	private void parseMatWireSize() throws IOException {
		float val = reader.getFloat();
		log("Mat WireSize : %f", val);
	}

	private void parseMatShading() throws IOException {
		int val = reader.getShort();
		log("Mat Shading : %d", val);
	}

	private void parseIntPercentage() throws IOException {
		int per = reader.getShort();
		log("Int Percentage : %d %%", per);
	}

	private void parseLinColorRGB() throws IOException {
		byte byteR = reader.getByteData();
		byte byteG = reader.getByteData();
		byte byteB = reader.getByteData();
		log("LIN_Color_24 : %d %d %d", byteR, byteG, byteB);
	}

	private void parseColorRGB() throws IOException {
		byte byteR = reader.getByteData();
		byte byteG = reader.getByteData();
		byte byteB = reader.getByteData();
		log("Color_24 : %d %d %d", byteR, byteG, byteB);
	}

	private void parseMasterScale() throws IOException {
		float scale = reader.getFloat();
		log("Master Scale : %f", scale);
	}

	// yhchung 140524 추가 끝

	private void parseMainChunk() {
		model = new Model();
	}

	private void parseMeshVersionChunk() throws IOException {
		int version = reader.getInt();
		log("Mesh version %d", version);
	}

	private void parseVersionChunk() throws IOException {
		int version = reader.getInt();
		log("3ds version %d", version);
	}

	private void parseObjectChunk() throws IOException {
		String name = reader.readString();
		log("Found Object %s", name);
		currentObject = model.newModelObject(name);
	}

	private void parseAngleData() throws IOException {
		float[] angleData = new float[3];
		for (int i = 0; i < 3; i++) {
			angleData[i] = reader.getFloat();
		}
		currentObject.angle = angleData;
		log("Found AngleData");
	}

	private void parseVerticesList() throws IOException {
		short numVertices = reader.getShort();
		log("Found %d vertices", numVertices);

		float[] vertices = new float[numVertices * 3];
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = reader.getFloat();
			log("vertex(%d): %f", i, vertices[i]);
		}
		currentObject.vertices = vertices;
	}

	private void parseFacesDescription() throws IOException {
		numFaces = reader.getShort();
		log("Found %d faces", numFaces);
		int flag = 0;
		short[] faces = new short[numFaces * 3];
		for (int i = 0; i < numFaces; i++) {
			faces[i * 3] = reader.getShort();
			faces[i * 3 + 1] = reader.getShort();
			faces[i * 3 + 2] = reader.getShort();
			flag = reader.getShort(); // Discard face flag
			log("Face(%d): %d, %d, %d, %d", i, faces[i * 3], faces[i * 3 + 1],
					faces[i * 3 + 2], flag);
		}
		currentObject.polygons = faces;
	}

	private void parseLocalCoordinateSystem() throws IOException {
		float[] x1 = new float[3];
		float[] x2 = new float[3];
		float[] x3 = new float[3];
		float[] origin = new float[3];

		log("Found LocalCoordinateSystem");
		readVector(x1);
		readVector(x2);
		readVector(x3);
		readVector(origin);
		log("vector1: %f %f %f", x1[0], x1[1], x1[2]);
		log("vector2: %f %f %f", x2[0], x2[1], x2[2]);
		log("vector2: %f %f %f", x3[0], x3[1], x3[2]);
		log("vectorOri: %f %f %f", origin[0], origin[1], origin[2]);
	}

	private void parseMappingCoordinates() throws IOException {
		short numVertices = reader.getShort();
		log("Found %d mapping coordinates", numVertices);

		float[] uv = new float[numVertices * 2];
		for (int i = 0; i < numVertices; i++) {
			uv[i * 2] = reader.getFloat();
			uv[i * 2 + 1] = reader.getFloat();
			log("MC(%d): %f, %f", i, uv[i * 2], uv[i * 2 + 1]);
		}
		currentObject.textureCoordinates = uv;
	}

	private void parseMaterialName() throws IOException {
		String materialName = reader.readString();
		log("Material name %s", materialName);
	}

	private void parseMappingFilename() throws IOException {
		String mappingFile = reader.readString();
		log("Mapping file %s", mappingFile);
	}

	private void readVector(float[] v) throws IOException {
		v[0] = reader.getFloat();
		v[1] = reader.getFloat();
		v[2] = reader.getFloat();
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	private void log(String format, Object... args) {
		if (logger != null) {
			logger.log(String.format(format, args));
		}
	}

}
